package com.orb.bmdadmin.data

import android.net.Uri
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class AddFoodViewModelFactory(
    private val foodForEdit: FoodItemStateUrl?
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AddFoodViewModel::class.java)) {
            return AddFoodViewModel(foodForEdit) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

class AddFoodViewModel(
    foodForEdit: FoodItemStateUrl? = null
) : ViewModel() {
    val foodList = SnapshotStateList<FoodItemInput>()
    val foodIndex = 0

    val optionInputs = mutableStateListOf<OptionStateInput>()

    val mergedItemsContainers = mutableStateMapOf<Int, MutableList<OptionState>>()

    init {
        if (foodForEdit == null) {
            addNewFood()
            addNewOptionInput()
        } else {
            convertToFoodItemInput(foodForEdit)
        }
    }

    fun addNewFood() {
        foodList.add(
            FoodItemInput(
                imgUri = mutableStateOf(null),
                imgUrl = mutableStateOf(""),
                foodName = mutableStateOf(""),
                price = mutableIntStateOf(0),
                availability = mutableStateOf(true),
                amount = mutableStateOf(null),
                options = mutableStateOf(getOptionStates())
            )
        )
    }

    fun getFoodItemState(): FoodItemStateUrl {
        val foodInput = foodList[foodIndex]

        val foodItem = FoodItemStateUrl(
            id = foodIndex,
            imgUrl = foodInput.imgUrl.value,
            foodName = foodInput.foodName.value,
            price = foodInput.price.value,
            availability = foodInput.availability.value,
            amount = foodInput.amount.value,
            options = foodInput.options.value
        )

        return foodItem
    }

    fun convertToFoodItemInput(foodItem: FoodItemStateUrl) {
        val foodItemInput =  FoodItemInput(
            imgUri = mutableStateOf(null),
            imgUrl = mutableStateOf(foodItem.imgUrl),
            foodName = mutableStateOf(foodItem.foodName),
            price = mutableStateOf(foodItem.price),
            availability = mutableStateOf(foodItem.availability),
            amount = mutableStateOf(foodItem.amount),
            options = mutableStateOf(foodItem.options)
        )

//        foodList[foodItem.id] = foodItemInput
        foodList.add(foodItemInput)
        optionInputs.addAll(getOptionInputState(foodItem.options))
        addToMergeContainers(foodItem.options)
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

    fun getOptionInputState(options: List<OptionState>?): List<OptionStateInput> {
        var inputOptions: List<OptionStateInput> = emptyList()
        if (options != null) {
            inputOptions =  options.map { option ->
                OptionStateInput(
                    name = mutableStateOf(option.name),
                    price = mutableStateOf(option.price),
                    amount = mutableStateOf(option.amount),
                    upperLimit = mutableStateOf(option.upperLimit),
                    lowerLimit = mutableStateOf(option.lowerLimit),
                    mergeGroup = mutableStateOf(option.mergeGroup),
                    mergeId = mutableStateOf(option.mergeId)
                )
            }
        } else {
            inputOptions = emptyList()
        }

        return inputOptions
    }

    fun addToMergeContainers(options: List<OptionState>?) {
        if (options != null) {
            for (option in options) {
                if (option.mergeGroup != null) {
                    addToMergeGroup(option.mergeGroup, listOf(option))
                }
            }
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

    fun removeFromMergeContainers(itemId: Int) {
        mergedItemsContainers.forEach { map ->
            for (item in map.value) {
                if (itemId == item.id) {
                    removeFromMergeGroup(groupId = map.key, items = listOf(item))
                }
            }
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
        val imgUri: MutableState<Uri?>,
        val imgUrl: MutableState<String>,
        val foodName: MutableState<String>,
        val price: MutableState<Int>,
        val availability: MutableState<Boolean>,
        val amount: MutableState<Int?>,
        val options: MutableState<List<OptionState>?>
    )
}