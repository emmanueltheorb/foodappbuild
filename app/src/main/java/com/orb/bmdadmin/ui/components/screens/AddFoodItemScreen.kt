package com.orb.bmdadmin.ui.components.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.AsyncImage
import coil3.compose.rememberAsyncImagePainter
import coil3.request.ImageRequest
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.integration.compose.placeholder
import com.orb.bmdadmin.R
import com.orb.bmdadmin.data.AddFoodViewModel
import com.orb.bmdadmin.data.AddFoodViewModelFactory
import com.orb.bmdadmin.data.FoodItemStateUrl
import com.orb.bmdadmin.data.Foods
import com.orb.bmdadmin.data.OptionState
import com.orb.bmdadmin.repository.StorageRepository
import com.orb.bmdadmin.ui.components.MyIntTextField
import com.orb.bmdadmin.ui.components.MyNullableTextField
import com.orb.bmdadmin.ui.components.MySnackBar
import com.orb.bmdadmin.ui.components.MyTextField
import com.orb.bmdadmin.ui.components.rememberMultiSelectionState
import com.orb.bmdadmin.ui.components.sections.BoxForCheckButton
import com.orb.bmdadmin.ui.components.sections.OptionInputSection
import kotlinx.coroutines.launch
import java.net.URLDecoder

@Composable
fun AddFoodItemScreen(
    modifier: Modifier = Modifier,
    foodForEdit: Foods? = null,
    imgUrl: String = "",
    onNavigate: () -> Unit
) {
    val addFoodViewModel: AddFoodViewModel =
        viewModel(factory = AddFoodViewModelFactory(foodForEdit))

    val addFoodScreenState = addFoodViewModel.addFoodScreenState
    val scope = rememberCoroutineScope()
    val scaffoldState = rememberScrollState()
    val snackBarHostState = remember { SnackbarHostState() }

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
    val onFirstNextClicked = {
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
        val lastOptionIndex = mutableIntStateOf(optionInputs.size - 1)
        addFoodViewModel.removeFromMergeContainers(lastOptionIndex.intValue)
        addFoodViewModel.optionInputs.removeAt(lastOptionIndex.intValue)
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
    LaunchedEffect(optionList) {
        addFoodViewModel.optionState = addFoodViewModel.getOptionStates().toMutableStateList()
    }
    var selectedImage by remember { mutableStateOf<Uri?>(foodInput.imgUri.value) }
    val launcher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri ->
            selectedImage = uri
        }
    val onDoneClicked: () -> Unit = {
        if (foodForEdit != null) {
            addFoodViewModel.updateFoodState(foodForEdit.documentId)
            onNavigate.invoke()
        } else {
            if (addFoodViewModel.foodList.last().imgUri.value != null) {
                scope.launch {
                    addFoodViewModel.addFoodState()
                    onNavigate.invoke()
                }
            } else {
                onNavigate()
            }
        }
    }

    Scaffold(
        snackbarHost = {
            SnackbarHost(
                hostState = snackBarHostState,
                snackbar = { snackBarData ->
                    MySnackBar(
                        snackBarData = snackBarData
                    )
                }
            )
        }
    ) { padding ->
        Box(
            modifier.fillMaxSize(),
            contentAlignment = Alignment.BottomEnd
        ) {
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .background(color = MaterialTheme.colorScheme.background)
//                .padding(top = 50.dp)
                    .padding(padding),
                verticalArrangement = Arrangement.spacedBy(18.dp)
            ) {
                if (addFoodScreenState.foodAddedStatus) {
                    scope.launch {
                        snackBarHostState
                            .showSnackbar(
                                message = "Added Food Successfully"
                            )
                        addFoodViewModel.resetFoodAddedStatus()
                        onNavigate.invoke()
                    }
                }

                if (addFoodScreenState.foodUpdatedStatus) {
                    scope.launch {
                        snackBarHostState
                            .showSnackbar("Food Updated Successfully")
                        addFoodViewModel.resetFoodAddedStatus()
                        onNavigate.invoke()
                    }
                }

                AvailableFoodItemInput(
                    foodImage = selectedImage,
                    imageUrl = imgUrl,
                    foodName = foodInput.foodName,
                    foodPrice = foodInput.price,
                    onAddImageClicked = {
                        launcher.launch("image/*")
                    },
                    onUriChanged = {
                        foodInput.imgUri.value = it
                    }
                )
                AvailabilitySectionInput(
                    amountAvailable = foodInput.amount
                )
                OptionInputSection(
                    modifier.weight(1f),
                    onIncreaseButtonClicked = onIncreaseButtonClicked,
                    onDecreaseButtonClicked = onDecreaseButtonClicked,
                    onTickClicked = onFirstNextClicked,
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
                    onEditClicked = onEditClicked,
                    onYesClicked = {
                        addFoodViewModel.addNewOptionInput()
                    }
                )
            }
            BoxForCheckButton(onTickClicked = onDoneClicked)
        }
    }
}

@Composable
private fun AvailableFoodItemInput(
    modifier: Modifier = Modifier,
    foodImage: Uri?,
    imageUrl: String,
    onAddImageClicked: () -> Unit,
    onUriChanged: (Uri?) -> Unit,
    foodName: MutableState<String>,
    foodPrice: MutableState<Int>
) {
    Column(
        modifier
            .padding(horizontal = 18.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        FoodImageInput(
            foodImage = foodImage,
            imageUrl = imageUrl,
            onAddImageClicked = onAddImageClicked,
            onUriChanged = onUriChanged
        )
        ItemDescriptionInput(
            foodName = foodName,
            foodPrice = foodPrice
        )
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
private fun FoodImageInput(
    modifier: Modifier = Modifier,
    foodImage: Uri?,
    imageUrl: String,
    onUriChanged: (Uri?) -> Unit,
    onAddImageClicked: () -> Unit
) {
    onUriChanged(foodImage)
    Card(
        modifier = modifier
            .clickable(onClick = onAddImageClicked)
            .fillMaxWidth()
            .height(250.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        if (foodImage != null) {
            Image(
                modifier = modifier.fillMaxSize(),
                painter = rememberAsyncImagePainter(foodImage),
                contentDescription = null,
                contentScale = ContentScale.Crop
            )
        }
        if (imageUrl.isNotEmpty()) {
            GlideImage(
                modifier = modifier.fillMaxSize(),
                model = imageUrl,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                loading = placeholder(R.drawable.right_ic),
                failure = placeholder(R.drawable.ic_close)
            )
        }
        if (foodImage == null && imageUrl.isEmpty()) {
            AddImageButton()
        }
    }
}

@Composable
fun AddImageButton(
    modifier: Modifier = Modifier
) {
    Box(
        modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .border(
                width = 2.dp,
                color = MaterialTheme.colorScheme.onBackground,
                shape = RoundedCornerShape(16.dp)
            ),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            modifier = modifier
                .size(20.dp),
            painter = painterResource(R.drawable.ic_plus),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onBackground
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