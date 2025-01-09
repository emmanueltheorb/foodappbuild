package com.example.app.data

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel

class OptionStateViewModel : ViewModel() {
    val optionInputs = SnapshotStateList<OptionStateInput>()

    init {
        addNewOptionInput()
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

    data class OptionStateInput(
        val name: MutableState<String>,
        val price: MutableState<Int>,
        val amount: MutableState<Int?>,
        val upperLimit: MutableState<Int?>,
        val lowerLimit: MutableState<Int?>,
        val mergeGroup: MutableState<Int?>,
        val mergeId: MutableState<Int?>
    )
}