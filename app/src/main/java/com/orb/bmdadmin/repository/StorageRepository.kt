package com.orb.bmdadmin.repository

import android.net.Uri
import android.util.Log
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.AggregateSource
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.orb.bmdadmin.data.Foods
import com.orb.bmdadmin.data.OptionState
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import java.util.UUID

const val FOODS_COLLECTION_REF = "foods"

class StorageRepository() {
    fun user() = Firebase.auth.currentUser
    fun hasUser(): Boolean = Firebase.auth.currentUser != null

    fun getUserId(): String = Firebase.auth.currentUser?.uid.orEmpty()

    private val foodsRef: CollectionReference = Firebase
        .firestore.collection(FOODS_COLLECTION_REF)

    suspend fun getFoodId(): Int {
        return try {
            val countQuery = foodsRef.count()
            val snapshot = countQuery.get(AggregateSource.SERVER).await()
            (snapshot.count).toInt().coerceAtLeast(0)
        } catch (e: Exception) {
            0
        }
    }

    fun uploadImageToFirebase(
        uri: Uri?,
        onSuccess: (String) -> Unit,
        onUploadError: (String) -> Unit
    ) {
        val storageReference =
            FirebaseStorage.getInstance().reference.child("images/${UUID.randomUUID()}")
        val uploadTask = storageReference.putFile(uri!!)

        uploadTask
            .addOnSuccessListener {
                storageReference.downloadUrl.addOnSuccessListener { downloadedUrl ->
                    onSuccess(downloadedUrl.toString())
                }
            }
            .addOnFailureListener {
                Log.e("Upload", "Image upload failed: ${it.message}")
                onUploadError(it.message.toString())
            }
    }


    fun getFoodList(): Flow<Resources<List<Foods>>> = callbackFlow {
        var snapshotStateListener: ListenerRegistration? = null

        try {
            snapshotStateListener = foodsRef
                .orderBy("id", Query.Direction.DESCENDING)
                .addSnapshotListener { snapshot, e ->
                    val response = if (snapshot != null) {
                        val foods = snapshot.toObjects(Foods::class.java)
                        Resources.Success(data = foods)
                    } else {
                        Resources.Error(throwable = e?.cause)
                    }
                    trySend(response)
                }
        } catch (e: Exception) {
            trySend(Resources.Error(e.cause))
            e.printStackTrace()
        }

        awaitClose {
            snapshotStateListener?.remove()
        }
    }

    fun getFood(
        foodId: String,
        onError: (Throwable?) -> Unit,
        onSuccess: (Foods?) -> Unit
    ) {
        foodsRef
            .document(foodId)
            .get()
            .addOnSuccessListener {
                onSuccess.invoke(it?.toObject(Foods::class.java))
            }
            .addOnFailureListener { result ->
                onError.invoke(result.cause)
            }
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
        val documentId = foodsRef.document().id
        val food = Foods(
            id,
            imgUrl,
            foodName,
            price,
            availability,
            amount,
            options,
            documentId
        )
        foodsRef
            .document(documentId)
            .set(food)
            .addOnCompleteListener { result ->
                onComplete.invoke(result.isSuccessful)
            }
    }

    fun deleteFood(foodId: String, onComplete: (Boolean) -> Unit) {
        foodsRef.document(foodId)
            .delete()
            .addOnCompleteListener {
                onComplete.invoke(it.isSuccessful)
            }
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
        val updateData = myHashMapOf(
            "id" to id,
            "imgUrl" to imgUrl,
            "foodName" to foodName,
            "price" to price,
            "availability" to availability,
            "amount" to amount,
            "options" to options
        )

        foodsRef.document(documentId)
            .update(updateData)
            .addOnCompleteListener {
                onResult.invoke(it.isSuccessful)
            }
    }

    private fun myHashMapOf(
        id: Pair<String, Int>,
        imgUrl: Pair<String, String>,
        foodName: Pair<String, String>,
        price: Pair<String, Int>,
        availability: Pair<String, Boolean>,
        amount: Pair<String, Int?>,
        options: Pair<String, List<OptionState>?>,
    ): HashMap<String, Any?> {
        return hashMapOf(
            id.first to id.second,
            imgUrl.first to imgUrl.second,
            foodName.first to foodName.second,
            price.first to price.second,
            availability.first to availability.second,
            amount.first to amount.second,
            options.first to options.second
        )
    }

    fun signOut() = Firebase.auth.signOut()
}

sealed class Resources<T>(
    val data: T? = null,
    val throwable: Throwable? = null
) {
    class Loading<T> : Resources<T>()
    class Success<T>(data: T?) : Resources<T>(data)
    class Error<T>(throwable: Throwable?) : Resources<T>(throwable = throwable)
}