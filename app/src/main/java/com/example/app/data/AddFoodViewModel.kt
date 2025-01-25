package com.example.app.data

import androidx.annotation.DrawableRes
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import com.example.app.data.OptionStateViewModel.OptionStateInput

class AddFoodViewModel : ViewModel() {
    val foodList = SnapshotStateList<FoodItemInput>()
    val foodIndex = 0

    val optionInputs = mutableStateListOf<OptionStateInput>()

    val mergedItemsContainers = mutableStateMapOf<Int, MutableList<OptionState>>()

    init {
        addNewFood()
        addNewOptionInput()
    }

    fun addNewFood() {
        foodList.add(
            FoodItemInput(
                img = mutableIntStateOf(0),
                foodName = mutableStateOf(""),
                price = mutableIntStateOf(0),
                availability = mutableStateOf(true),
                amount = mutableStateOf(null),
                options = mutableStateOf(getOptionStates())
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

    fun convertToFoodItemInput(foodItem: FoodItemState): FoodItemInput {
        val foodItemInput =  FoodItemInput(
            img = mutableStateOf(foodItem.img),
            foodName = mutableStateOf(foodItem.foodName),
            price = mutableStateOf(foodItem.price),
            availability = mutableStateOf(foodItem.availability),
            amount = mutableStateOf(foodItem.amount),
            options = mutableStateOf(foodItem.options)
        )
//        foodList[foodItem.id] = foodItemInput
        foodList.add(foodItemInput)

        return foodItemInput
    }

    fun addNewOptionInput() {
        optionInputs.add(
            OptionStateInput(
                name = mutableStateOf(""),
                price = mutableIntStateOf(0),
                amount = mutableStateOf(null),
                upperLimit = mutableStateOf(null),
                lowerLimit = mutableStateOf(null),
                mergeGroup = mutableStateOf(null),
                mergeId = mutableStateOf(null)
            )
        )
    }

    fun getOptionStates(): List<OptionState> {
        return optionInputs.mapIndexed { index, input ->
            OptionState(
                id = index,
                name = input.name.value,
                price = input.price.value,
                amount = input.amount.value,
                upperLimit = input.upperLimit.value,
                lowerLimit = input.lowerLimit.value,
                mergeGroup = input.mergeGroup.value,
                mergeId = input.mergeId.value
            )
        }
    }

    fun addToMergeGroup(groupId: Int, items: List<OptionState>) {
        mergedItemsContainers[groupId] =
            (mergedItemsContainers[groupId] ?: mutableListOf()).apply {
                addAll(items)
            }
        items.forEachIndexed { index, item ->
            optionInputs[item.id].mergeGroup.value = groupId
            optionInputs[item.id].mergeId.value = index
        }
    }

    fun removeFromMergeGroup(groupId: Int, items: List<OptionState>) {
        mergedItemsContainers[groupId]?.removeAll(items)
        items.forEach { item ->
            optionInputs[item.id].mergeGroup.value = null
            optionInputs[item.id].mergeId.value = null
        }
    }

    fun deleteOption(options: List<OptionState>) {
        for (option in options) {
            optionInputs.removeAt(option.id)
        }
    }

    data class OptionStateInput(
        val name: MutableState<String>,
        val price: MutableState<Int>,
        val amount: MutableState<Int?>,
        val upperLimit: MutableState<Int?>,
        val lowerLimit: MutableState<Int?>,
        val mergeGroup: MutableState<Int?>,
        val mergeId: MutableState<Int?>
    )

    data class FoodItemInput(
        @DrawableRes val img: MutableState<Int>,
        val foodName: MutableState<String>,
        val price: MutableState<Int>,
        val availability: MutableState<Boolean>,
        val amount: MutableState<Int?>,
        val options: MutableState<List<OptionState>?>
    )
}