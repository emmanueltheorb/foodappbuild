package com.example.app.data

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel

class OptionStateViewModel: ViewModel() {
    val optionInputs = mutableStateListOf<OptionStateInput>(
        OptionStateInput(
            name = mutableStateOf("A"),
            price = mutableIntStateOf(1),
            amount = mutableStateOf(null),
            upperLimit = mutableStateOf(null),
            lowerLimit = mutableStateOf(null),
            mergeGroup = mutableStateOf(null),
            mergeId = mutableStateOf(null)
        ),
        OptionStateInput(
            name = mutableStateOf("B"),
            price = mutableIntStateOf(2),
            amount = mutableStateOf(null),
            upperLimit = mutableStateOf(null),
            lowerLimit = mutableStateOf(null),
            mergeGroup = mutableStateOf(null),
            mergeId = mutableStateOf(null)
        ),
        OptionStateInput(
            name = mutableStateOf("C"),
            price = mutableIntStateOf(3),
            amount = mutableStateOf(null),
            upperLimit = mutableStateOf(null),
            lowerLimit = mutableStateOf(null),
            mergeGroup = mutableStateOf(null),
            mergeId = mutableStateOf(null)
        ),
        OptionStateInput(
            name = mutableStateOf("D"),
            price = mutableIntStateOf(4),
            amount = mutableStateOf(null),
            upperLimit = mutableStateOf(null),
            lowerLimit = mutableStateOf(null),
            mergeGroup = mutableStateOf(null),
            mergeId = mutableStateOf(null)
        ),
        OptionStateInput(
            name = mutableStateOf("E"),
            price = mutableIntStateOf(5),
            amount = mutableStateOf(null),
            upperLimit = mutableStateOf(null),
            lowerLimit = mutableStateOf(null),
            mergeGroup = mutableStateOf(null),
            mergeId = mutableStateOf(null)
        ),
        OptionStateInput(
            name = mutableStateOf("F"),
            price = mutableIntStateOf(6),
            amount = mutableStateOf(null),
            upperLimit = mutableStateOf(null),
            lowerLimit = mutableStateOf(null),
            mergeGroup = mutableStateOf(null),
            mergeId = mutableStateOf(null)
        )
    )

    val mergedItemsContainers = mutableStateMapOf<Int, MutableList<OptionState>>()


    init {
//        addNewOptionInput()
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