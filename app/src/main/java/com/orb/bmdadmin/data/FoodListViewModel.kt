package com.orb.bmdadmin.data

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.orb.bmdadmin.repository.Resources
import com.orb.bmdadmin.repository.StorageRepository
import kotlinx.coroutines.launch

class FoodListViewModel (
    private val repository: StorageRepository = StorageRepository()
): ViewModel() {
    var foodListScreenState by mutableStateOf(FoodListScreenState())

    val user = repository.user()
    val hasUser: Boolean
        get() = repository.hasUser()
    private val userId: String
        get() = repository.getUserId()

    fun getFoods() {
        if (hasUser) {
            viewModelScope.launch {
                repository.getFoodList().collect {
                    foodListScreenState = foodListScreenState.copy(foodsList = it)
                }
            }
        } else {
            foodListScreenState = foodListScreenState.copy(foodsList = Resources.Error(
                throwable = Throwable(message = "User is not logged In")
            ))
        }
    }

    fun deleteFood(documentId: String) {
        repository.deleteFood(documentId) {
            foodListScreenState = foodListScreenState.copy(noteDeletedStatus = it)
        }
    }
}

data class FoodListScreenState(
    val foodsList: Resources<List<Foods>> = Resources.Loading(),
    val noteDeletedStatus: Boolean = false
)