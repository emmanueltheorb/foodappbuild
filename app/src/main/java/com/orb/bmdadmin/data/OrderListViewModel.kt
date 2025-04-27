package com.orb.bmdadmin.data

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.orb.bmdadmin.repository.Resources
import com.orb.bmdadmin.repository.StorageRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.time.Duration.Companion.hours

@HiltViewModel
class OrderListViewModel @Inject constructor(
    private val networkMonitor: NetworkMonitor,
    private val repository: StorageRepository = StorageRepository()
) : ViewModel() {
    val isOnline = networkMonitor.isOnline
    private val _orderListScreenState = MutableStateFlow(OrderListScreenState())
    val orderListScreenState: StateFlow<OrderListScreenState> = _orderListScreenState.asStateFlow()

    private var foodCollectionJob: Job? = null

    val hasUser: Boolean
        get() = repository.hasUser()

    init {
        getFoods()
    }

    private fun getFoods() {
        foodCollectionJob?.cancel()

        foodCollectionJob = viewModelScope.launch {
            repository.getReservedFoods()
                .collect { resource ->
                    when (resource) {
                        is Resources.Loading -> {
                            // Handle loading state if needed
                        }
                        is Resources.Success -> {
                            val validFoods = resource.data?.filter {
                                System.currentTimeMillis() - it.reservedAt <= 14.hours.inWholeMilliseconds
                            } ?: emptyList()

                            _orderListScreenState.update {
                                it.copy(
                                    allFoods = validFoods.filter { it.status != "received" },
                                    orderList = validFoods.filter { it.status == "ordered" },
                                    reserveList = validFoods.filter { it.status == "reserved" },
                                    receivedList = validFoods.filter { it.status == "received" }
                                )
                            }
                        }
                        is Resources.Error -> {
                            // Handle error state
                            Log.e("OrderListVM", "Error fetching foods", resource.throwable)
                        }
                    }
                }
        }
    }

    fun moveToReceived(documentId: String, status: String) {
        repository.moveToReceived(documentId, status)
        _orderListScreenState.update { state ->
            state.copy(
                snackBarMessage = SnackBarMessage.Undoable(
                    message = "Moved to Received",
                    actionLabel = "UNDO",
                    documentId = documentId,
                    lastStatus = status
                )
            )
        }
    }

    fun undoMoveToReceived(documentId: String, lastStatus: String) {
        repository.updateFoodStatus(documentId, lastStatus)
        _orderListScreenState.update { state ->
            state.copy(
                snackBarMessage = SnackBarMessage.Success("Item Restored")
            )
        }
    }

    fun updateFoodStatus(documentId: String, newStatus: String) {
        repository.updateFoodStatus(documentId, newStatus)
    }

    fun undoDelete(deleteItem: ReservedFood, index: Int) {
        val currentOrderList = _orderListScreenState.value.orderList.toMutableList()
        val currentReceivedList = _orderListScreenState.value.receivedList.toMutableList()

        // Remove from received list
        currentReceivedList.remove(deleteItem)

        // Safely re-insert into order list
        if (index >= 0 && index <= currentOrderList.size) {
            // Insert at original index if valid
            currentOrderList.add(index, deleteItem)
        } else {
            // If index is out of bounds (e.g., list was empty or shrank), append to end
            currentOrderList.add(deleteItem)
        }

        _orderListScreenState.update { state ->
            state.copy(
                orderList = currentOrderList,
                receivedList = currentReceivedList,
                snackBarMessage = SnackBarMessage.Success("Item Restored")
            )
        }
    }

    fun restoreItemFromReceived(item: ReservedFood) {
        val currentReceivedList = _orderListScreenState.value.receivedList.toMutableList()
        if (currentReceivedList.remove(item)) { // Remove the specific item
            val currentOrderList = _orderListScreenState.value.orderList.toMutableList()
            currentOrderList.add(item) // Append to the end of orderList

            _orderListScreenState.update { state ->
                state.copy(
                    orderList = currentOrderList,
                    receivedList = currentReceivedList,
                    snackBarMessage = SnackBarMessage.Success("Item Restored")
                )
            }
        }
    }

    fun clearSnackBar() {
        _orderListScreenState.update { it.copy(snackBarMessage = null) }
    }

}

data class OrderListScreenState(
    val allFoods: List<ReservedFood> = emptyList<ReservedFood>(),
    val orderList: List<ReservedFood> = emptyList<ReservedFood>(),
    val reserveList: List<ReservedFood> = emptyList<ReservedFood>(),
    val receivedList: List<ReservedFood> = emptyList<ReservedFood>(),
    val snackBarMessage: SnackBarMessage? = null
)