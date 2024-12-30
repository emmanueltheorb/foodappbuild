package com.example.app.data

data class ReservingScreenState(
    val foodItemId: Int = 3,
    val foodsList: List<FoodItemState> = FoodItemsData,
    var options: Unit? = null
)