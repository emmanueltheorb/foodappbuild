package com.orb.bmdadmin.data

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.database.FirebaseDatabase
import com.orb.bmdadmin.repository.Resources
import com.orb.bmdadmin.repository.StorageRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FoodListViewModel @Inject constructor(
    private val networkMonitor: NetworkMonitor,
    private val repository: StorageRepository = StorageRepository()
) : ViewModel() {
    val isOnline = networkMonitor.isOnline

    private val _foodListScreenState = MutableStateFlow(FoodListScreenState())
    val foodListScreenState: StateFlow<FoodListScreenState> = _foodListScreenState.asStateFlow()


    private var foodCollectionJob: Job? = null

    val user = repository.user()
    val hasUser: Boolean
        get() = repository.hasUser()
    private val userId: String
        get() = repository.getUserId()

    fun getFoods() {
        foodCollectionJob?.cancel() // Cancel previous collection

        if (hasUser) {
            foodCollectionJob = viewModelScope.launch {
                repository.getFoodList().collect { resources ->
                    _foodListScreenState.update { currentState ->
                        currentState.copy(foodsList = resources)
                    }
                }
            }
        } else {
            _foodListScreenState.update { currentState ->
                currentState.copy(
                    foodsList = Resources.Error(
                        throwable = Throwable("User is not logged In")
                    )
                )
            }
        }
    }

    fun deleteFood(documentId: String, imgUrl: String) {
        repository.deleteFood(documentId, imgUrl) {
            _foodListScreenState.update { currentState ->
                currentState.copy(foodDeletedStatus = it)
            }
        }
    }

    fun updateFoodAmountsBatch(amountMap: Map<String, String>) {
        viewModelScope.launch {
            try {
                val updates = mutableMapOf<String, Any>()

                amountMap.forEach { (documentId, newAmount) ->
                    if (newAmount.isNotEmpty() && documentId.isNotBlank()) {
                        val amountValue = newAmount.toIntOrNull() ?: 0
                        updates["/foods/$documentId/amount"] = amountValue
                    }
                }

                if (updates.isNotEmpty()) {
                    if (isOnline.value) {
                        repository.database.updateChildren(updates).addOnCompleteListener {
                            if (it.isSuccessful) {
                                showSnackBar(SnackBarMessage.Success("Amounts Updated"))
                            } else {
                                showSnackBar(
                                    SnackBarMessage.Error("Update failed: ${it.exception?.message}")
                                )
                            }
                        }
                    } else {
                        showSnackBar(
                            SnackBarMessage.Error("You're Offline")
                        )
                    }
                }
            } catch (e: Exception) {
                showSnackBar(SnackBarMessage.Error("Update error: ${e.localizedMessage}"))
            }
        }
    }

    fun changeAvailability(documentId: String, availability: Boolean) {
        viewModelScope.launch {
            try {
                repository.database.child("foods").child(documentId).child("availability")
                    .setValue(!availability).addOnCompleteListener {
                        if (it.isSuccessful) {

                        } else {
                            showSnackBar(SnackBarMessage.Error("Update failed: ${it.exception?.message}"))
                        }
                    }
            } catch (e: Exception) {
                showSnackBar(SnackBarMessage.Error("Update error: ${e.localizedMessage}"))
            }
        }
    }

    private fun showSnackBar(message: SnackBarMessage) {
        _foodListScreenState.update { currentState ->
            currentState.copy(snackBarMessage = message)
        }
    }

    fun clearSnackBar() {
        _foodListScreenState.update { currentState ->
            currentState.copy(snackBarMessage = null)
        }
    }
}

data class FoodListScreenState(
    val foodsList: Resources<List<Foods>> = Resources.Loading(),
    val foodDeletedStatus: Boolean = false,
    val snackBarMessage: SnackBarMessage? = null
)

sealed class SnackBarMessage(
    val message: String,
    val isError: Boolean = false
) {
    class Success(message: String) : SnackBarMessage(message)
    class Error(message: String) : SnackBarMessage(message, isError = true)
    class Undoable(
        message: String,
        val actionLabel: String,
        val documentId: String,
        val lastStatus: String
    ): SnackBarMessage(message)
}