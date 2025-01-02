package com.example.app.data

import androidx.annotation.DrawableRes
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel

class AddFoodViewModel : ViewModel() {
    val foodList = SnapshotStateList<FoodItemInput>()
    val foodIndex = 0

    init {
        addNewFood()
    }

    fun addNewFood() {
        foodList.add(
            FoodItemInput(
                img = mutableIntStateOf(0),
                foodName = mutableStateOf(""),
                price = mutableIntStateOf(0),
                availability = mutableStateOf(true),
                amount = mutableStateOf(null),
                options = mutableStateOf(null)
            )
        )
    }

    fun getFoodItemState(): FoodItemState {
        val foodInput = foodList[foodIndex]

        val foodItem = FoodItemState(
            id = foodIndex,
            img = foodInput.img.value,
            foodName = foodInput.foodName.value,
            price = foodInput.price.value,
            availability = foodInput.availability.value,
            amount = foodInput.amount.value,
            options = foodInput.options.value
        )

        return foodItem
    }

    data class FoodItemInput(
        @DrawableRes val img: MutableState<Int>,
        val foodName: MutableState<String>,
        val price: MutableState<Int>,
        val availability: MutableState<Boolean>,
        val amount: MutableState<Int?>,
        val options: MutableState<MutableList<OptionState>?>
    )
}