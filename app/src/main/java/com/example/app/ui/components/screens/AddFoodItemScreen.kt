package com.example.app.ui.components.screens

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.app.R
import com.example.app.data.AddFoodViewModel
import com.example.app.data.OptionState
import com.example.app.ui.components.MyIntTextField
import com.example.app.ui.components.MyNullableTextField
import com.example.app.ui.components.MyTextField
import com.example.app.ui.components.rememberMultiSelectionState
import com.example.app.ui.components.sections.OptionInputSection
import com.example.app.ui.theme.AppTheme

@Composable
fun AddFoodItemScreen(
    modifier: Modifier = Modifier,
    @DrawableRes foodImage: Int,
    addFoodViewModel: AddFoodViewModel = viewModel()
) {
    val foodInput = addFoodViewModel.foodList[addFoodViewModel.foodIndex]
    val state = rememberMultiSelectionState()
    val optionInputs = addFoodViewModel.optionInputs
    val optionList = addFoodViewModel.getOptionStates().toMutableStateList()
    var signal by remember { mutableIntStateOf(1) }
    var signalForPopUp by remember { mutableIntStateOf(1) }
    var signalForAddIcon by remember { mutableStateOf(false) }
    val onAddClicked = {
        signal = 1
    }
    val onTickClicked = {
        signal = 2
    }
    val onRightClicked = {
        signal = 3
    }
    val onEditClicked = {
        signal = 2
    }
    val onIncreaseButtonClicked: () -> Unit = {
        addFoodViewModel.addNewOptionInput()
    }
    val onDecreaseButtonClicked: () -> Unit = {
        addFoodViewModel.optionInputs.removeAt(optionInputs.size - 1)
    }
    val selectedItems = remember {
        mutableStateListOf<OptionState>()
    }
    val selectedItemsInBox = remember {
        mutableStateListOf<OptionState>()
    }
    val mergedItemsContainers = addFoodViewModel.mergedItemsContainers
    var filteredItems by remember { mutableStateOf(optionList.toList()) }
    var mergeGroupNumber by remember { mutableIntStateOf(0) }
    var mergedItems by remember { mutableStateOf(emptyList<OptionState>()) }

    val onSelectedItems: (List<OptionState>) -> Unit = {
        if (it.size >= 2 || selectedItemsInBox.isNotEmpty()) {
            signalForPopUp = 2
        } else {
            signalForPopUp = 1
        }
        if (it.isNotEmpty()) {
            signalForAddIcon = true
        }
        if (it.size == 1) {
            signalForPopUp = -1
        }
    }
    val onSelectedItemsInBox: (List<OptionState>) -> Unit = {

    }
    val onMergeClicked: () -> Unit = {
        addFoodViewModel.addToMergeGroup(mergeGroupNumber, selectedItems)
        selectedItems.clear()
        mergeGroupNumber++
        state.isMultiSelectionModeEnabled = !state.isMultiSelectionModeEnabled
    }
    val onRemoveClicked: () -> Unit = {
        if (selectedItemsInBox.isNotEmpty()) {
            selectedItemsInBox.forEachIndexed { index, item ->
                addFoodViewModel.removeFromMergeGroup(
                    addFoodViewModel.optionInputs[item.id].mergeGroup.value!!,
                    listOf(item)
                )
            }
        }
        selectedItemsInBox.clear()
        if (selectedItems.isNotEmpty()) {
            addFoodViewModel.deleteOption(selectedItems)
        }
        selectedItems.clear()
        state.isMultiSelectionModeEnabled = !state.isMultiSelectionModeEnabled
    }
    val onDeleteClicked = {
        addFoodViewModel.deleteOption(selectedItems)
        selectedItems.clear()
        state.isMultiSelectionModeEnabled = !state.isMultiSelectionModeEnabled
    }
    val onAddIconClicked: (Int) -> Unit = { groupId ->
        if (selectedItems.isNotEmpty()) {
            addFoodViewModel.addToMergeGroup(groupId, selectedItems)
        }
        selectedItems.clear()
        state.isMultiSelectionModeEnabled = !state.isMultiSelectionModeEnabled
    }
    LaunchedEffect(optionList, mergedItemsContainers) {
        snapshotFlow {
            Pair(optionList.toList(), mergedItemsContainers.flatMap { it.value })
        }.collect { (updatedOptionList, updatedMergedContainer) ->
            mergedItems = updatedMergedContainer
            filteredItems = updatedOptionList.filterNot { item ->
                mergedItems.any { mergedItem ->
                    mergedItem.id == item.id
                }
            }
        }
    }


    Column(
        modifier = modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background)
            .padding(top = 50.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        AvailableFoodItemInput(
            foodImage = foodImage,
            foodName = foodInput.foodName,
            foodPrice = foodInput.price
        )
        AvailabilitySectionInput(
            amountAvailable = foodInput.amount
        )
        OptionInputSection(
            modifier.weight(1f),
            onIncreaseButtonClicked = onIncreaseButtonClicked,
            onDecreaseButtonClicked = onDecreaseButtonClicked,
            onTickClicked = onTickClicked,
            optionInputs = optionInputs,
            state = state,
            optionList = optionList,
            mergeGroupNumber = mergeGroupNumber,
            signalForAddIcon = signalForAddIcon,
            onAddIconClicked = onAddIconClicked,
            onMergeClicked = onMergeClicked,
            onRemoveClicked = onRemoveClicked,
            onDeleteClicked = onDeleteClicked,
            signal = signal,
            signalForPopUp = signalForPopUp,
            selectedItems = selectedItems,
            selectedItemsInBox = selectedItemsInBox,
            mergedItemsContainers = mergedItemsContainers,
            filteredItems = filteredItems,
            onSelectedItems = onSelectedItems,
            onSelectedItemsInBox = onSelectedItemsInBox,
            onAddClicked = onAddClicked,
            onRightClicked = onRightClicked,
            onEditClicked = onEditClicked
        )
    }
}

@Composable
private fun AvailableFoodItemInput(
    modifier: Modifier = Modifier,
    @DrawableRes foodImage: Int,
    foodName: MutableState<String>,
    foodPrice: MutableState<Int>
) {
    Column(
        modifier
        .padding(horizontal = 18.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        FoodImageInput(
            foodImage = foodImage
        )
        ItemDescriptionInput(
            foodName = foodName,
            foodPrice = foodPrice
        )
    }
}

@Composable
private fun FoodImageInput(
    modifier: Modifier = Modifier,
    @DrawableRes foodImage: Int
) {
    val onFoodItemClicked = {}

    Card(
        modifier = modifier
            .clickable(onClick = onFoodItemClicked)
            .fillMaxWidth()
            .height(250.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Image(
            modifier = modifier.fillMaxSize(),
            painter = painterResource(foodImage),
            contentDescription = null,
            contentScale = ContentScale.Crop
        )
    }
}

@Composable
private fun ItemDescriptionInput(
    modifier: Modifier = Modifier,
    foodName: MutableState<String>,
    foodPrice: MutableState<Int>
) {
    Row(
        modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        MyTextField(
            placeholder = "Food name",
            textValue = foodName
        )
        MyIntTextField(
            placeholder = "Price",
            textValue = foodPrice,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
    }
}

@Composable
private fun AvailabilitySectionInput(
    modifier: Modifier = Modifier,
    amountAvailable: MutableState<Int?>
) {
    Row(
        modifier
            .padding(horizontal = 18.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Text(
            text = "Available: ",
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.bodyMedium
        )
        MyNullableTextField(
            placeholder = "amount",
            textValue = amountAvailable.value?.toString() ?: "",
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            onValueChange = {
                if (!it.isEmpty()) {
                    amountAvailable.value = it.toInt()
                } else {
                    amountAvailable.value = null
                }
            }
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun AddFoodItemScreenPreview() {
    AppTheme(darkTheme = true) {
        AddFoodItemScreen(foodImage = R.drawable.pizza)
    }
}