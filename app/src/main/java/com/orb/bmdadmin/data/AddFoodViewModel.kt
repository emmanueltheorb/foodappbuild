package com.orb.bmdadmin.data

import android.net.Uri
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseUser
import com.orb.bmdadmin.repository.StorageRepository

class AddFoodViewModelFactory(
    private val foodForEdit: Foods?
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AddFoodViewModel::class.java)) {
            return AddFoodViewModel(
                foodForEdit
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

class AddFoodViewModel(
    foodForEdit: Foods? = null,
    private val repository: StorageRepository = StorageRepository()
) : ViewModel() {
    var addFoodScreenState by mutableStateOf(AddFoodScreenState())

    private val hasUser: Boolean
        get() = repository.hasUser()

    private val user: FirebaseUser?
        get() = repository.user()

    val foodList = SnapshotStateList<FoodItemInput>()
    val foodIndex = 0
    val updateIndexPosition: MutableState<Int?> = mutableStateOf(null)
    val imageUrl = foodForEdit?.imgUrl

    val optionInputs = mutableStateListOf<OptionStateInput>()

    val mergedItemsContainers = mutableStateMapOf<Int, MutableList<OptionState>>()

    init {
        foodList.clear()
        optionInputs.clear()
        if (foodForEdit == null) {
            resetState()
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

    var optionState: List<OptionState> = emptyList()

    suspend fun addFoodState() {
        val foodInput = foodList.last()
        val id = repository.getFoodId()

        repository.uploadImageToFirebase(
            uri = foodInput.imgUri.value,
            onSuccess = { url ->
                foodInput.imgUrl.value = url
                val foodItem = FoodItemStateUrl(
                    id = id,
                    imgUrl = url,
                    foodName = foodInput.foodName.value,
                    price = foodInput.price.value,
                    availability = foodInput.availability.value,
                    amount = foodInput.amount.value,
                    options = optionState
                )

                if (hasUser) {
                    repository.addFood(
                        id = foodItem.id,
                        imgUrl = foodItem.imgUrl,
                        foodName = foodItem.foodName,
                        price = foodItem.price,
                        availability = foodItem.availability,
                        amount = foodItem.amount,
                        options = foodItem.options
                    ) {
                        addFoodScreenState = addFoodScreenState.copy(foodAddedStatus = it)
                    }
                }
            },
            onUploadError = { error ->
                if (error.isNotEmpty()) {
                    addFoodScreenState = addFoodScreenState.copy(imageUploadError = true)
                } else {
                    addFoodScreenState = addFoodScreenState.copy(imageUploadError = false)
                }
            }
        )
    }

    fun updateFoodState(documentId: String) {
        val foodInput = foodList.last()

        val foodItem = FoodItemStateUrl(
            id = updateIndexPosition.value!!,
            imgUrl = foodInput.imgUrl.value,
            foodName = foodInput.foodName.value,
            price = foodInput.price.value,
            availability = foodInput.availability.value,
            amount = foodInput.amount.value,
            options = foodInput.options.value
        )

        repository.updateFood(
            id = foodItem.id,
            imgUrl = foodItem.imgUrl,
            foodName = foodItem.foodName,
            price = foodItem.price,
            availability = foodItem.availability,
            amount = foodItem.amount,
            options = foodItem.options,
            documentId = documentId
        ) {
            addFoodScreenState = addFoodScreenState.copy(foodUpdatedStatus = it)
        }
    }

    fun resetFoodAddedStatus() {
        addFoodScreenState =
            addFoodScreenState.copy(foodAddedStatus = false, foodUpdatedStatus = false)
    }

    fun resetState() {
        addFoodScreenState = AddFoodScreenState()
    }

    fun convertToFoodItemInput(foodItem: Foods) {
        updateIndexPosition.value = foodItem.id

        val foodItemInput = FoodItemInput(
            imgUri = mutableStateOf(null),
            imgUrl = mutableStateOf(foodItem.imgUrl),
            foodName = mutableStateOf(foodItem.foodName),
            price = mutableStateOf(foodItem.price),
            availability = mutableStateOf(foodItem.availability),
            amount = mutableStateOf(foodItem.amount),
            options = mutableStateOf(foodItem.options)
        )

        foodList.add(foodItemInput)
        optionInputs.addAll(getOptionInputState(foodItem.options))
        addToMergeContainers(foodItem.options)
    }

    fun addNewOptionInput() {
        optionInputs.add(
            OptionStateInput(
                id = mutableStateOf(optionInputs.size),
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
                id = input.id.value,
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
            inputOptions = options.map { option ->
                OptionStateInput(
                    id = mutableStateOf(option.id),
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
        val idsToRemove = options.map { it.id }
        optionInputs.removeAll { input ->
            input.id.value in idsToRemove
        }
    }

    data class OptionStateInput(
        val id: MutableState<Int>,
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

data class AddFoodScreenState(
    val foodAddedStatus: Boolean = false,
    val foodUpdatedStatus: Boolean = false,
    val imageUploadError: Boolean = false
)