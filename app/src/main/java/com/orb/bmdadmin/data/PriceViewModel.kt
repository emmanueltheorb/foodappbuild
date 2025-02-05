package com.orb.bmdadmin.data

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class PriceViewModel(): ViewModel() {
    private val _prices = MutableStateFlow<List<Int>>(emptyList())
    val prices: StateFlow<List<Int>> get() = _prices

    fun addPrice(price: Int) {
        _prices.value = _prices.value + price
    }

    val sumOfPrices: StateFlow<Int> =
        _prices.map { it.sum() }
        .stateIn(viewModelScope,
            SharingStarted.Lazily, 0)
}

//class PriceViewModelFactory(screenState: ReservingScreenState): ViewModelProvider.Factory {
//    override fun <T : ViewModel> create(modelClass: Class<T>): T {
//        if (modelClass.isAssignableFrom(PriceViewModel::class.java)) {
//            @Suppress("UNCHECKED_CAST")
//            return PriceViewModel(ReservingScreenState()) as T
//        }
//        throw IllegalArgumentException("Unknown ViewModel class")
//    }
//}