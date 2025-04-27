package com.orb.bmdadmin.repository

import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import android.net.Uri
import android.util.Log
import com.google.firebase.auth.ktx.auth
import com.orb.bmdadmin.data.Foods
import com.orb.bmdadmin.data.OptionState
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.MutableData
import com.google.firebase.database.Transaction
import com.google.firebase.database.ktx.getValue
import com.orb.bmdadmin.data.ReservedFood
import kotlinx.coroutines.tasks.await
import java.util.UUID
import javax.inject.Inject

const val FOODS_NODE = "foods"
const val RESERVES_NODE = "reserves"
const val ADMINS_NODE = "admins"
const val ID_COUNTER_NODE = "metadata/foodIdCounter"
const val ADMIN_CONTROL_NODE = "admin_control"
const val USERS_NODE = "users"

class StorageRepository @Inject constructor() {
    val database = Firebase.database.reference
    private val foodsRef = database.child(FOODS_NODE)
    private val idCounterRef = database.child(ID_COUNTER_NODE)
    private val reservesRef = database.child(RESERVES_NODE)
    private val adminsRef = database.child(ADMINS_NODE)
    private val adminControlRef = database.child(ADMIN_CONTROL_NODE)
    private val usersRef = database.child(USERS_NODE)

    fun user() = Firebase.auth.currentUser
    fun hasUser(): Boolean = Firebase.auth.currentUser != null
    fun getUserId(): String = Firebase.auth.currentUser?.uid.orEmpty()

    fun getFoodId(callback: (Int) -> Unit) {
        idCounterRef.runTransaction(object : Transaction.Handler {
            override fun doTransaction(currentData: MutableData): Transaction.Result {
                val currentId = currentData.getValue(Int::class.java) ?: -1
                currentData.value = currentId + 1
                return Transaction.success(currentData)
            }

            override fun onComplete(
                error: DatabaseError?,
                committed: Boolean,
                data: DataSnapshot?
            ) {
                if (committed) {
                    val newId = data?.getValue(Int::class.java) ?: 0
                    callback(newId)
                } else {
                    callback(0)
                }
            }
        })
    }

    fun addToAdminList(
        email: String,
        userId: String
    ) {
        val adminIds = hashMapOf<String, Any>(
            "email" to email,
            "uid" to userId
        )
        adminsRef.setValue(adminIds)
    }

    fun generateRandomCode(): Int {
        return (10000000..99999999).random()
    }

    fun storeCodeInDB(code: Int) {
        adminControlRef.child("latest_code").setValue(
            hashMapOf(
                "code" to code,
                "timestamp" to ServerValue.TIMESTAMP
            )
        )
    }

    fun checkAdminAccess(onResult: (Boolean) -> Unit) {
        adminControlRef.child("code_generator").child("uid")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    onResult(snapshot.getValue(String::class.java) == getUserId())
                }

                override fun onCancelled(error: DatabaseError) {

                }
            })
    }

    fun verifyOTP(inputCode: Int, onResult: (Boolean) -> Unit) {
        adminControlRef.child("latest_code").addListenerForSingleValueEvent(
            object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val storedCode = snapshot.child("code").value

                    if (storedCode == inputCode) {
                        adminControlRef.child("latest_code").runTransaction(
                            object : Transaction.Handler {
                                override fun doTransaction(currentData: MutableData): Transaction.Result {
                                    currentData.value = null
                                    return Transaction.success(currentData)
                                }

                                override fun onComplete(
                                    error: DatabaseError?,
                                    committed: Boolean,
                                    currentData: DataSnapshot?
                                ) {
                                    if (committed) {
                                        onResult(true)
                                    } else {
                                        onResult(false)
                                    }
                                }

                            }
                        )
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("OTP", "Verification failed", error.toException())
                }

            }
        )
    }

    fun getOTP(
        otp: (Int) -> Unit
    ) {
        adminControlRef.child("latest_code").child("code")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    otp(snapshot.getValue(Int::class.java) ?: 10000000)
                }

                override fun onCancelled(error: DatabaseError) {

                }
            })
    }

    fun uploadImageToFirebase(
        uri: Uri?,
        onSuccess: (String) -> Unit,
        onUploadError: (String) -> Unit
    ) {
        val storageReference = FirebaseStorage.getInstance().reference
            .child("images/${UUID.randomUUID()}")
        val uploadTask = storageReference.putFile(uri!!)

        uploadTask.addOnSuccessListener {
            storageReference.downloadUrl.addOnSuccessListener { downloadedUrl ->
                onSuccess(downloadedUrl.toString())
            }
        }.addOnFailureListener {
            onUploadError(it.message.toString())
        }
    }

    fun getFoodList(): Flow<Resources<List<Foods>>> = callbackFlow {
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val foods = if (snapshot.exists()) {
                    snapshot.children.mapNotNull { foodSnapshot ->
                        foodSnapshot.getValue(Foods::class.java)?.copy(
                            documentId = foodSnapshot.key ?: ""
                        )
                    }.sortedByDescending { it.id }
                } else {
                    emptyList()
                }
                trySend(Resources.Success(foods))
            }

            override fun onCancelled(error: DatabaseError) {
                trySend(Resources.Error(error.toException()))
            }
        }

        foodsRef.addValueEventListener(listener)
        awaitClose { foodsRef.removeEventListener(listener) }
    }

    fun getReservesList(): Flow<Resources<List<Foods>>> = callbackFlow { }

    fun getFood(
        foodId: String,
        onError: (Throwable?) -> Unit,
        onSuccess: (Foods?) -> Unit
    ) {
        foodsRef.child(foodId).get()
            .addOnSuccessListener { snapshot ->
                val food = snapshot.getValue(Foods::class.java)
                onSuccess(food?.copy(documentId = snapshot.key ?: ""))
            }
            .addOnFailureListener { onError(it) }
    }

    fun addFood(
        id: Int,
        imgUrl: String,
        foodName: String,
        price: Int,
        availability: Boolean,
        amount: Int?,
        options: List<OptionState>?,
        onComplete: (Boolean) -> Unit
    ) {
        val newFoodRef = foodsRef.push()
        val documentId = newFoodRef.key ?: return

        val food = Foods(
            id = id,
            imgUrl = imgUrl,
            foodName = foodName,
            price = price,
            availability = availability,
            amount = amount,
            options = options ?: emptyList(),
            documentId = documentId
        )

        newFoodRef.setValue(food)
            .addOnCompleteListener { onComplete(it.isSuccessful) }
    }

    fun updateFood(
        id: Int,
        imgUrl: String,
        foodName: String,
        price: Int,
        availability: Boolean,
        amount: Int?,
        options: List<OptionState>?,
        documentId: String,
        onResult: (Boolean) -> Unit
    ) {
        val updates = hashMapOf<String, Any>(
            "id" to id,
            "imgUrl" to imgUrl,
            "foodName" to foodName,
            "price" to price,
            "availability" to availability,
            "amount" to (amount ?: 0),
            "options" to (options ?: emptyList<OptionState>())
        )

        foodsRef.child(documentId).updateChildren(updates)
            .addOnCompleteListener { onResult(it.isSuccessful) }
    }

    fun deleteFood(foodId: String, imgUrl: String, onComplete: (Boolean) -> Unit) {
        val storage = FirebaseStorage.getInstance()
        val imageRef = storage.getReferenceFromUrl(imgUrl)

        imageRef.delete()
            .addOnSuccessListener {
                foodsRef.child(foodId).removeValue()
                    .addOnCompleteListener { onComplete(it.isSuccessful) }
            }
            .addOnFailureListener {
                onComplete(false)
            }
    }

    fun getReservedFoods(): Flow<Resources<List<ReservedFood>>> = callbackFlow {
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val foods = if (snapshot.exists()) {
                    snapshot.children.mapNotNull { foodSnapshot ->
                        foodSnapshot.getValue(ReservedFood::class.java)?.copy(
                            documentId = foodSnapshot.key ?: ""
                        )
                    }.sortedByDescending { it.reservedAt }
                } else {
                    emptyList()
                }
                trySend(Resources.Success(foods))
            }

            override fun onCancelled(error: DatabaseError) {
                trySend(Resources.Error(error.toException()))
            }
        }

        reservesRef.addValueEventListener(listener)
        awaitClose { reservesRef.removeEventListener(listener) }
    }

    fun updateFoodStatus(documentId: String, newStatus: String) {
        reservesRef.child(documentId).child("status").setValue(newStatus)
    }

    fun moveToReceived(documentId: String, status: String) {
        reservesRef.child(documentId).child("status").setValue("received")
        reservesRef.child(documentId).child("lastStatus").setValue(status)
    }

    fun deleteReservedFood(documentId: String) {
        reservesRef.child(documentId).removeValue()
    }
}

sealed class Resources<T>(
    val data: T? = null,
    val throwable: Throwable? = null
) {
    class Loading<T> : Resources<T>()
    class Success<T>(data: T?) : Resources<T>(data)
    class Error<T>(throwable: Throwable?) : Resources<T>(throwable = throwable)
}