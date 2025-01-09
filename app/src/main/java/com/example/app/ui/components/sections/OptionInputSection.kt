package com.example.app.ui.components.sections

import androidx.annotation.DrawableRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.CardColors
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.app.R
import com.example.app.data.OptionState
import com.example.app.data.OptionStateViewModel
import com.example.app.data.OptionStateViewModel.OptionStateInput
import com.example.app.ui.components.MergedGroup
import com.example.app.ui.components.MultiSelectionList
import com.example.app.ui.components.MultiSelectionState
import com.example.app.ui.components.MyTextFieldWithCallback
import com.example.app.ui.components.PopUpForMergeAndRemove
import com.example.app.ui.components.rememberMultiSelectionState
import com.example.app.ui.theme.AppTheme
import com.example.app.ui.theme.RobotoFontFamily
import kotlin.collections.set

@Composable
fun OptionInputSection(
    modifier: Modifier = Modifier
) {
    OptionCheck()
}

@Composable
fun OptionCheck(
    modifier: Modifier = Modifier
) {
    var signal by remember { mutableStateOf(false) }
    val onButtonClicked = {
        signal = true
    }

    if (signal == false) {
        OptionCheckPrompt(onButtonClicked = onButtonClicked)
    } else {
        OptionModeSwitch()
    }
}

@Composable
private fun OptionCheckPrompt(
    modifier: Modifier = Modifier,
    onButtonClicked: () -> Unit
) {
    val buttonText by remember { mutableStateOf("Yes") }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.spacedBy(9.dp)
    ) {
        Text(
            text = "Does food have options?",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.End,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Button(
            modifier = modifier
                .wrapContentSize(),
            onClick = onButtonClicked,
            shape = RoundedCornerShape(7.dp),
            colors = ButtonColors(
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.onSurface,
                disabledContainerColor = MaterialTheme.colorScheme.surface,
                disabledContentColor = MaterialTheme.colorScheme.onSurface
            )
        ) {
            Text(
                modifier = modifier.wrapContentSize(),
                text = buttonText,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun OptionModeSwitch(
    modifier: Modifier = Modifier,
    optionStateViewModel: OptionStateViewModel = viewModel()
) {
    val state = rememberMultiSelectionState()
    val optionInputs = optionStateViewModel.optionInputs
    val optionList = optionStateViewModel.getOptionStates()
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
        optionStateViewModel.addNewOptionInput()
    }
    val onDecreaseButtonClicked: () -> Unit = {
        optionStateViewModel.optionInputs.removeAt(optionInputs.size - 1)
    }
    val selectedItems = remember {
        mutableStateListOf<OptionState>()
    }
    val selectedItemsInBox = remember {
        mutableStateListOf<OptionState>()
    }
    val mergedItemsContainers = remember {
        mutableStateMapOf<Int, MutableList<OptionState>>()
    }
    val filteredItems = optionList.filter {
        it !in mergedItemsContainers.values.flatten()
    }
    var mergeGroupNumber by remember { mutableIntStateOf(0) }
    val onSelectedItems: (List<OptionState>) -> Unit = {
        if (it.size >= 2 || selectedItemsInBox.isNotEmpty()) {
            signalForPopUp = 2
        } else {
            signalForPopUp = 1
        }
        if (it.isNotEmpty()) {
            signalForAddIcon = true
        }
    }
    val onSelectedItemsInBox: (List<OptionState>) -> Unit = {

    }
    val onMergeClicked: () -> Unit = {
        mergedItemsContainers[mergeGroupNumber] = selectedItems.toMutableList()
        selectedItems.forEachIndexed{ index, item ->
            optionInputs[item.id].mergeGroup.value = mergeGroupNumber
            optionInputs[item.id].mergeId.value = index
        }
        selectedItems.clear()
        mergeGroupNumber++
        state.isMultiSelectionModeEnabled = !state.isMultiSelectionModeEnabled
    }
    val onRemoveClicked: () -> Unit = {
        selectedItemsInBox.forEachIndexed { index, item ->
            mergedItemsContainers[item.mergeGroup]!!.removeAt(index)
            selectedItemsInBox.remove(item)
//            filteredItems.toMutableList().add(item)
        }
        state.isMultiSelectionModeEnabled = !state.isMultiSelectionModeEnabled
    }
    val onAddIconClicked: (Int) -> Unit = { groupId ->
        if (selectedItems.isNotEmpty()) {
            selectedItems.forEach { item ->
                mergedItemsContainers[groupId]!!.add(item)
            }
        }
    }

    if (signal == 1) {
        OptionsInput(
            onIncreaseButtonClicked = onIncreaseButtonClicked,
            onDecreaseButtonClicked = onDecreaseButtonClicked,
            onTickClicked = onTickClicked,
            optionInputs = optionInputs
        )
    }
    if (signal == 2) {
        OptionsInMultiSelectionList(
            optionList = optionList,
            state = state,
            mergeGroupNumber = mergeGroupNumber,
            signal = signalForPopUp,
            onMergeClicked = onMergeClicked,
            onRemoveClicked = onRemoveClicked,
            selectedItems = selectedItems,
            selectedItemsInBox = selectedItemsInBox,
            mergedItemsContainers = mergedItemsContainers,
            filteredItems = filteredItems,
            onSelectedItems = onSelectedItems,
            onSelectedItemsInBox = onSelectedItemsInBox,
            onAddClicked = onAddClicked,
            onRightClicked = onRightClicked,
            signalForAddIcon = signalForAddIcon,
            onAddIconClicked = onAddIconClicked
        )
    }
    if (signal == 3) {
        OptionsPreviewSection(
            options = optionList,
            onAddClicked = onAddClicked,
            onEditClicked = onEditClicked
        )
    }
}

@Composable
private fun OptionsInput(
    modifier: Modifier = Modifier,
    onIncreaseButtonClicked: () -> Unit,
    onDecreaseButtonClicked: () -> Unit,
    onTickClicked: () -> Unit,
    optionInputs: SnapshotStateList<OptionStateInput>
) {
    val scrollState = rememberScrollState()
    Box(
        modifier.fillMaxSize(),
        contentAlignment = Alignment.BottomEnd
    ) {
        Column(
            modifier
                .fillMaxSize()
                .background(color = MaterialTheme.colorScheme.background)
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            optionInputs.forEach { input ->
                OptionColumn(
                    input = input
                )
            }
            ButtonRow(
                onIncreaseButtonClicked = onIncreaseButtonClicked,
                onDecreaseButtonClicked = onDecreaseButtonClicked
            )
            Spacer(modifier = modifier.height(50.dp))
        }
        BoxForCheckButton(onTickClicked = onTickClicked)
    }
}

@Composable
fun BoxForCheckButton(
    modifier: Modifier = Modifier,
    onTickClicked: () -> Unit
) {
    Box(
        modifier
            .size(100.dp)
            .padding(horizontal = 14.dp)
            .padding(bottom = 20.dp),
        contentAlignment = Alignment.BottomEnd
    ) {
        CheckButton(onTickClicked = onTickClicked)
    }
}

@Composable
fun CheckButton(
    modifier: Modifier = Modifier,
    onTickClicked: () -> Unit
) {
    Surface(
        modifier = modifier
            .size(40.dp),
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.surface,
        contentColor = MaterialTheme.colorScheme.onSurface
    ) {
        Box(
            modifier = modifier
                .fillMaxSize()
                .clickable(onClick = onTickClicked)
                .padding(3.dp),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(id = R.drawable.check_mark_icon),
                contentDescription = null,
                modifier = modifier.size(30.dp)
            )
        }
    }
}

@Composable
fun OptionColumn(
    modifier: Modifier = Modifier,
    input: OptionStateInput
) {
    Column(
        modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(6.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OptionInputItem(input = input)
    }
}

@Composable
fun OptionsInMultiSelectionList(
    modifier: Modifier = Modifier,
    state: MultiSelectionState,
    optionList: List<OptionState>,
    mergeGroupNumber: Int,
    signalForAddIcon: Boolean,
    onAddIconClicked: (Int) -> Unit,
    onMergeClicked: () -> Unit,
    onRemoveClicked: () -> Unit,
    signal: Int,
    selectedItems: SnapshotStateList<OptionState>,
    selectedItemsInBox: SnapshotStateList<OptionState>,
    mergedItemsContainers: SnapshotStateMap<Int, MutableList<OptionState>>,
    filteredItems: List<OptionState>,
    onSelectedItems: (List<OptionState>) -> Unit,
    onSelectedItemsInBox: (List<OptionState>) -> Unit,
    onAddClicked: () -> Unit,
    onRightClicked: () -> Unit
) {
    val scrollState = rememberScrollState()

    Column(
        verticalArrangement = Arrangement.spacedBy(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextButton(
                text = "Add",
                icon = R.drawable.plus2,
                onClick = onAddClicked
            )
            TextButton(
                text = "Next",
                icon = R.drawable.right_ic,
                onClick = onRightClicked
            )
        }
        Column(
            modifier.verticalScroll(scrollState),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            mergedItemsContainers.forEach { (mergeGroupNumber, mergedItems) ->
                MergeBox(
                    mergeGroupNumber = mergeGroupNumber,
                    mergedItems = mergedItems,
                    state = state,
                    selectedItemsInBox = selectedItemsInBox,
                    onSelectedItemsInBox = onSelectedItemsInBox,
                    signalForAddIcon = signalForAddIcon,
                    onAddIconClicked = onAddIconClicked,
                )
            }
            MultiSelectionList(
                state = state,
                items = filteredItems,
                selectedItems = selectedItems,
                onSelectedItems = onSelectedItems,
                itemContent = {
                    if (it.amount == null) {
                        ListOptionWithoutAmount(option = it)
                    } else {
                        ListOptionWithAmount(option = it)
                    }
                },
                key = {
                    it.id
                },
                onClick = {
                    if (state.isMultiSelectionModeEnabled) {
                        if (selectedItems.contains(it)) {
                            selectedItems.remove(it)
                        } else {
                            selectedItems.add(it)
                        }
                    }
                }
            )
        }
    }
    if (signal == 2) {
        PopUpForMergeAndRemove(
            onMergeClicked = onMergeClicked,
            onRemoveClicked = onRemoveClicked
        )
    }
}

@Composable
fun MergeBox(
    modifier: Modifier = Modifier,
    mergeGroupNumber: Int,
    signalForAddIcon: Boolean,
    onAddIconClicked: (Int) -> Unit,
    selectedItemsInBox: SnapshotStateList<OptionState>,
    onSelectedItemsInBox: (List<OptionState>) -> Unit,
    mergedItems: MutableList<OptionState>,
    state: MultiSelectionState,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        if (signalForAddIcon == true) {
            PopUpAddButton(
                onAddIconClicked = onAddIconClicked,
                mergeGroupNumber = mergeGroupNumber
            )
        }
        Spacer(Modifier.height(4.dp))
        MergedGroup(
            state = state,
            items = mergedItems,
            selectedItems = selectedItemsInBox,
            key = {
                it.id
            },
            onSelectedItems = onSelectedItemsInBox,
            itemContent = {
                if (it.amount == null) {
                    Row(
                        modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = it.name,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                        Spacer(modifier.width(82.dp))
                        Text(
                            text = "₦${it.price}",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    }
                } else {
                    Row(
                        modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = it.name,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                        AmountInfo(option = it)
                        Text(
                            text = "₦${it.price}",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    }
                }
            },
            onClick = {
                if (state.isMultiSelectionModeEnabled) {
                    if (selectedItemsInBox.contains(it)) {
                        selectedItemsInBox.remove(it)
                    } else {
                        selectedItemsInBox.add(it)
                    }
                }
            }
        )
    }
}

@Composable
fun PopUpAddButton(
    modifier: Modifier = Modifier,
    onAddIconClicked: (Int) -> Unit,
    mergeGroupNumber: Int
) {
    Row(
        modifier.padding(end = 7.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Spacer(modifier.weight(1f))
        MyIconButton2(
            icon = R.drawable.ic_plus,
            onClicked = onAddIconClicked,
            mergeGroupNumber = mergeGroupNumber
        )
    }
}

@Composable
fun MyIconButton2(
    modifier: Modifier = Modifier,
    @DrawableRes icon: Int,
    containerColor: Color = MaterialTheme.colorScheme.surface,
    onClicked: (Int) -> Unit,
    mergeGroupNumber: Int
) {
    Surface(
        modifier = modifier
            .size(24.dp)
            .clickable{
                onClicked.invoke(mergeGroupNumber)
            },
        shape = CircleShape,
        color = containerColor,
        contentColor = MaterialTheme.colorScheme.onSurface
    ) {
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(5.dp),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(icon),
                contentDescription = null
            )
        }
    }
}

@Composable
fun OptionsPreviewSection(
    modifier: Modifier = Modifier,
    options: List<OptionState>,
    onAddClicked: () -> Unit,
    onEditClicked: () -> Unit
) {
    val scrollState = rememberScrollState()

    Column(
        verticalArrangement = Arrangement.spacedBy(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextButton(
                text = "Add",
                icon = R.drawable.plus2,
                onClick = onAddClicked
            )
            TextButton(
                text = "Edit",
                icon = R.drawable.edit_icon,
                onClick = onEditClicked
            )
        }
        OptionsSection(
            modifier.verticalScroll(scrollState),
            options = options.toMutableList()
        )
    }
}

@Composable
fun TextButton(
    modifier: Modifier = Modifier,
    text: String,
    @DrawableRes icon: Int,
    onClick: () -> Unit
) {
    Row(
        modifier.clickable(onClick = onClick),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground
        )
        Icon(
            modifier = modifier.size(15.dp),
            painter = painterResource(icon),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onBackground
        )
    }
}

@Composable
fun AmountInfo(
    modifier: Modifier = Modifier,
    option: OptionState
) {
    Row(
        modifier = modifier.wrapContentWidth(),
        horizontalArrangement = Arrangement.spacedBy(5.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IntSurface(
            value = option.lowerLimit
        )
        Text(
            text = option.amount.toString(),
            color = MaterialTheme.colorScheme.onBackground
        )
        IntSurface(
            value = option.upperLimit
        )
    }
}

@Composable
fun ListOptionWithoutAmount(
    modifier: Modifier = Modifier,
    option: OptionState
) {
    val scrollState = rememberScrollState()

    Row(
        modifier = modifier
            .fillMaxWidth()
            .horizontalScroll(scrollState),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Surface(
            text = option.name,
            width = 100.dp
        )
        Spacer(modifier.width(50.dp))
        PriceTextView(
            price = option.price,
            width = 100.dp
        )
    }
}

@Composable
fun ListOptionWithAmount(
    modifier: Modifier = Modifier,
    option: OptionState
) {
    val scrollState = rememberScrollState()

    Row(
        modifier = modifier
            .fillMaxWidth()
            .horizontalScroll(scrollState),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Surface(
            text = option.name,
            width = 100.dp
        )
        AmountInfo(option = option)
        PriceTextView(
            price = option.price,
            width = 100.dp
        )
    }
}

@Composable
fun ListSurface(
    modifier: Modifier = Modifier,
    text: String
) {
    OutlinedCard(
        modifier = Modifier
            .height(35.dp)
            .width(125.dp)
            .padding(16.dp),
        border = BorderStroke(width = 2.dp, color = MaterialTheme.colorScheme.onBackground),
        colors = CardColors(
            containerColor = MaterialTheme.colorScheme.background,
            contentColor = MaterialTheme.colorScheme.onBackground,
            disabledContentColor = MaterialTheme.colorScheme.onBackground,
            disabledContainerColor = MaterialTheme.colorScheme.background,
        )
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .widthIn(100.dp)
                .height(35.dp)
                .padding(5.dp)
        ) {
            Text(
                text = text,
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

@Composable
fun ListPriceView(
    modifier: Modifier = Modifier,
    price: Int
) {
    ListSurface(text = "₦$price")
}

@Composable
fun ButtonRow(
    modifier: Modifier = Modifier,
    onIncreaseButtonClicked: () -> Unit,
    onDecreaseButtonClicked: () -> Unit
) {
    Row(
        modifier.wrapContentSize(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        OptionButton(
            icon = R.drawable.ic_minus,
            containerColor = MaterialTheme.colorScheme.surfaceBright,
            onClicked = onDecreaseButtonClicked
        )
        OptionButton(
            icon = R.drawable.ic_plus,
            containerColor = MaterialTheme.colorScheme.surface,
            onClicked = onIncreaseButtonClicked
        )
    }
}

@Composable
fun OptionInputItem(
    modifier: Modifier = Modifier,
    input: OptionStateInput
) {
    Column(
        modifier.background(MaterialTheme.colorScheme.background),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(
            modifier = modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            NameInput(textValue = input.name)
            AmountInput(input = input)
            PriceInput(textValue = input.price)
        }
    }
}

@Composable
fun OptionButton(
    modifier: Modifier = Modifier,
    @DrawableRes icon: Int,
    containerColor: Color = MaterialTheme.colorScheme.surface,
    contentColor: Color = MaterialTheme.colorScheme.onSurface,
    onClicked: () -> Unit
) {
    Surface(
        modifier = modifier.size(35.dp),
        shape = CircleShape,
        color = containerColor,
        contentColor = MaterialTheme.colorScheme.onSurface
    ) {
        Box(
            modifier = modifier
                .fillMaxSize()
                .clickable(onClick = onClicked),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                modifier = modifier.size(20.dp),
                painter = painterResource(icon),
                contentDescription = null,
                tint = contentColor
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun OptionInputItemPreview() {
    AppTheme {
        Box(
            Modifier
                .fillMaxSize()
                .background(
                    color = MaterialTheme.colorScheme.background
                ),
            contentAlignment = Alignment.Center
        ) {
            OptionCheck()
        }
    }
}

@Composable
fun NameInput(
    modifier: Modifier = Modifier,
    textValue: MutableState<String>,
    placeholder: String = "NAME",
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default
) {

    OutlinedCard(
        modifier = Modifier
            .width(125.dp)
            .padding(16.dp),
        border = BorderStroke(width = 2.dp, color = MaterialTheme.colorScheme.onBackground),
        colors = CardColors(
            containerColor = MaterialTheme.colorScheme.background,
            contentColor = MaterialTheme.colorScheme.onBackground,
            disabledContentColor = MaterialTheme.colorScheme.onBackground,
            disabledContainerColor = MaterialTheme.colorScheme.background,
        )
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .width(125.dp)
                .height(50.dp)
                .padding(5.dp)
        ) {
            BasicTextField(
                modifier = modifier
                    .wrapContentSize(),
                value = textValue.value,
                onValueChange = {
                    textValue.value = it
                },
                textStyle = TextStyle(
                    color = MaterialTheme.colorScheme.onBackground,
                    fontFamily = RobotoFontFamily,
                    fontWeight = FontWeight.Normal,
                    fontSize = 20.sp
                ),
                cursorBrush = SolidColor(
                    MaterialTheme.colorScheme.onBackground
                ),
                decorationBox = { innerTextField ->
                    Box(
                        contentAlignment = Alignment.Center
                    ) {
                        if (textValue.value.isEmpty()) {
                            Text(
                                text = placeholder,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onBackground,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                        innerTextField()
                    }
                },
                keyboardOptions = keyboardOptions
            )
        }
    }
}

@Composable
fun PriceInput(
    modifier: Modifier = Modifier,
    textValue: MutableState<Int>,
    placeholder: String = "PRICE",
    keyboardOptions: KeyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
) {

    OutlinedCard(
        modifier = Modifier
            .width(125.dp)
            .padding(16.dp),
        border = BorderStroke(width = 2.dp, color = MaterialTheme.colorScheme.onBackground),
        colors = CardColors(
            containerColor = MaterialTheme.colorScheme.background,
            contentColor = MaterialTheme.colorScheme.onBackground,
            disabledContentColor = MaterialTheme.colorScheme.onBackground,
            disabledContainerColor = MaterialTheme.colorScheme.background,
        )
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .width(125.dp)
                .height(50.dp)
                .padding(5.dp)
        ) {
            BasicTextField(
                modifier = modifier
                    .wrapContentSize(),
                value = if (textValue.value == 0) {
                    ""
                } else {
                    textValue.value.toString()
                },
                onValueChange = {
                    textValue.value = it.toIntOrNull() ?: 0
                },
                textStyle = TextStyle(
                    color = MaterialTheme.colorScheme.onBackground,
                    fontFamily = RobotoFontFamily,
                    fontWeight = FontWeight.Normal,
                    fontSize = 20.sp
                ),
                cursorBrush = SolidColor(
                    MaterialTheme.colorScheme.onBackground
                ),
                decorationBox = { innerTextField ->
                    Box(
                        contentAlignment = Alignment.Center
                    ) {
                        if (textValue.value <= 0) {
                            Text(
                                text = placeholder,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onBackground,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                        innerTextField()
                    }
                },
                keyboardOptions = keyboardOptions
            )
        }
    }
}

@Composable
fun AmountInput(
    modifier: Modifier = Modifier,
    input: OptionStateInput
) {
    var signal by remember { mutableStateOf(false) }
    val onButtonClicked = {
        signal = true
    }

    if (signal == false) {
        AmountText(onAmountTextClicked = onButtonClicked)
    } else {
        AmountDetails(input = input)
    }
}

@Composable
fun AmountText(
    modifier: Modifier = Modifier,
    onAmountTextClicked: () -> Unit
) {
    Text(
        modifier = modifier.clickable(onClick = onAmountTextClicked),
        text = "add amount",
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.onBackground
    )
}

@Composable
fun AmountDetails(
    modifier: Modifier = Modifier,
    input: OptionStateInput
) {
    var isExpanded by remember {
        mutableStateOf(false)
    }
    val onClick = {
        isExpanded = true
    }

    Box(
        modifier = modifier
            .clickable(
                onClick = onClick
            ),
        contentAlignment = Alignment.Center
    ) {
        ConstrainLayoutForAmount(
            amountValue = input.amount.value,
            upperLimitValue = input.upperLimit.value,
            lowerLimitValue = input.lowerLimit.value
        )

        DropdownMenu(
            modifier = modifier
                .wrapContentSize()
                .background(MaterialTheme.colorScheme.background),
            expanded = isExpanded,
            onDismissRequest = { isExpanded = false }
        ) {
            AmountValueInput(
                input = input,
                onAmountValueChanged = {
                    if (!it.isEmpty()) {
                        input.amount.value = it.toInt()
                    } else {
                        input.amount.value = null
                    }
                },
                onUpperLimitValueChanged = {
                    if (!it.isEmpty()) {
                        input.upperLimit.value = it.toInt()
                    } else {
                        input.upperLimit.value = null
                    }
                },
                onLowerLimitValueChanged = {
                    if (!it.isEmpty()) {
                        input.lowerLimit.value = it.toInt()
                    } else {
                        input.lowerLimit.value = null
                    }
                }
            )
        }
    }
}

@Composable
fun ConstrainLayoutForAmount(
    modifier: Modifier = Modifier,
    amountValue: Int?,
    upperLimitValue: Int?,
    lowerLimitValue: Int?
) {
    ConstraintLayout(
        modifier = modifier
            .width(85.dp)
            .height(42.dp)
    ) {
        val (amount, upperLimit, lowerLimit) = createRefs()

        IntSurface(
            value = amountValue,
            modifier = modifier
                .constrainAs(amount) {
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
        )

        IntSurface(
            value = lowerLimitValue,
            containerColor = MaterialTheme.colorScheme.background,
            contentColor = MaterialTheme.colorScheme.onBackground,
            modifier = modifier
                .constrainAs(lowerLimit) {
                    start.linkTo(parent.start, margin = 10.dp)
                    bottom.linkTo(parent.bottom, margin = 18.dp)
                }
        )

        IntSurface(
            value = upperLimitValue,
            containerColor = MaterialTheme.colorScheme.background,
            contentColor = MaterialTheme.colorScheme.onBackground,
            modifier = modifier
                .constrainAs(upperLimit) {
                    end.linkTo(parent.end, margin = 10.dp)
                    bottom.linkTo(parent.bottom, margin = 18.dp)
                }
        )
    }
}

@Composable
fun AmountValueInput(
    modifier: Modifier = Modifier,
    input: OptionStateInput,
    onAmountValueChanged: (String) -> Unit,
    onUpperLimitValueChanged: (String) -> Unit,
    onLowerLimitValueChanged: (String) -> Unit
) {
    OutlinedCard(
        modifier = modifier
            .wrapContentSize(),
        border = BorderStroke(width = 2.dp, color = MaterialTheme.colorScheme.onBackground),
        colors = CardColors(
            containerColor = MaterialTheme.colorScheme.background,
            contentColor = MaterialTheme.colorScheme.onBackground,
            disabledContentColor = MaterialTheme.colorScheme.onBackground,
            disabledContainerColor = MaterialTheme.colorScheme.background,
        )
    ) {
        Column(
            modifier = modifier
                .padding(vertical = 8.dp)
                .padding(horizontal = 15.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            MyTextFieldWithCallback(
                placeholder = "Amount",
                value = input.amount.value?.toString() ?: "",
                onValueChange = onAmountValueChanged,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
            HorizontalDivider(modifier.width(124.dp))
            MyTextFieldWithCallback(
                placeholder = "UpperLimit",
                value = input.upperLimit.value?.toString() ?: "",
                onValueChange = onUpperLimitValueChanged,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
            HorizontalDivider(modifier.width(124.dp))
            MyTextFieldWithCallback(
                placeholder = "LowerLimit",
                value = input.lowerLimit.value?.toString() ?: "",
                onValueChange = onLowerLimitValueChanged,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
        }
    }
}

//@Preview(showBackground = true, showSystemUi = true)
//@Composable
//private fun Preview() {
//    AppTheme {
//        Box(
//            Modifier.fillMaxSize(),
//            contentAlignment = Alignment.Center
//        ) {
//            OptionInputItem()
//        }
//    }
//}

@Composable
fun IntSurface(
    modifier: Modifier = Modifier,
    value: Int?,
    containerColor: Color = MaterialTheme.colorScheme.surface,
    contentColor: Color = MaterialTheme.colorScheme.onSurface,
    borderColor: BorderStroke = BorderStroke(
        width = 1.dp,
        color = MaterialTheme.colorScheme.surface
    )
) {
    Surface(
        modifier = modifier.size(24.dp),
        shape = CircleShape,
        color = containerColor,
        contentColor = contentColor,
        border = borderColor
    ) {
        Box(
            modifier = modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = if (value == null) {
                    "0"
                } else {
                    value.toString()
                },
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}