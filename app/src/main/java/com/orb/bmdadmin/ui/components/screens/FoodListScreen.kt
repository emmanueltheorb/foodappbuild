package com.orb.bmdadmin.ui.components.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.orb.bmdadmin.R
import com.orb.bmdadmin.data.FoodItemState
import com.orb.bmdadmin.data.FoodListScreenState
import com.orb.bmdadmin.data.FoodListViewModel
import com.orb.bmdadmin.data.Foods
import com.orb.bmdadmin.repository.Resources
import com.orb.bmdadmin.ui.components.CustomSnackBarVisuals
import com.orb.bmdadmin.ui.components.FoodListItem
import com.orb.bmdadmin.ui.components.MySnackBar
import com.orb.bmdadmin.ui.components.PopUpForMergeAndRemove
import com.orb.bmdadmin.ui.components.SearchButton
import com.orb.bmdadmin.ui.theme.AppTheme
import kotlinx.coroutines.launch
import java.net.URLEncoder

@Composable
fun FoodListScreen(
    modifier: Modifier = Modifier,
    foodListViewModel: FoodListViewModel = hiltViewModel(),
    navToAddFoodScreen: () -> Unit,
    onEditFoodClicked: (food: Foods, imgUrl: String) -> Unit,
    onSearchButtonClicked: () -> Unit
) {
    val screenState by foodListViewModel.foodListScreenState.collectAsState()
    var showPopUp by remember { mutableStateOf(false) }
    var foodForEdit: Foods? by remember { mutableStateOf(null) }
    var imgUrl by remember { mutableStateOf("") }
    val amountMap = remember { mutableStateMapOf<String, String>() }
    val buttonSignal by remember {
        derivedStateOf {
            amountMap.values.any { it.isNotEmpty() }
        }
    }
    val scope = rememberCoroutineScope()
    val snackBarMessage = screenState.snackBarMessage
    val snackBarHostState = remember { SnackbarHostState() }
    val keyboardController = LocalSoftwareKeyboardController.current

    LaunchedEffect(Unit) {
        foodListViewModel.getFoods()
        amountMap.clear()
    }

    Scaffold(
        snackbarHost = {
            SnackbarHost(
                hostState = snackBarHostState,
                modifier = modifier
                    .navigationBarsPadding(),
                snackbar = { data ->
                    MySnackBar(snackBarData = data)
                }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentAlignment = Alignment.BottomEnd
        ) {
            Content1(
                modifier.fillMaxSize(),
                screenState = screenState,
                amountMap = amountMap,
                foodForEdit = { food, string ->
                    foodForEdit = food
                    imgUrl = string
                },
                onFoodInListClicked = {
                    showPopUp = true
                },
                onSearchButtonClicked = onSearchButtonClicked,
                onAmountChange = { docId, newAmount ->
                    amountMap[docId] = newAmount
                },
                onAvailabilityChange = { boolean, docId ->
                    foodListViewModel.changeAvailability(docId, boolean)
                }
            )
            BoxForAddButton(
                onAddButtonClicked = {
                    navToAddFoodScreen.invoke()
                }
            )
            if (buttonSignal) {
                Row(
                    modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .padding(horizontal = 18.dp)
                        .padding(bottom = 12.dp)
                        .imePadding() // Adjust layout for keyboard
                        .navigationBarsPadding(),
                    horizontalArrangement = Arrangement.End
                ) {
                    UpdateAmountButton {
                        foodListViewModel.updateFoodAmountsBatch(amountMap.toMap())
                        amountMap.clear()
                        keyboardController?.hide()
                    }
                }
            }
            if (showPopUp) {
                PopUpForMergeAndRemove(
                    firstString = "Edit",
                    secondString = "Delete",
                    onMergeClicked = {
                        onEditFoodClicked.invoke(
                            foodForEdit!!.copy(
                                imgUrl = URLEncoder.encode(foodForEdit!!.imgUrl, "UTF-8")
                            ),
                            imgUrl
                        )
                        showPopUp = false
                    }
                    ,
                    onRemoveClicked = {
                        foodListViewModel.deleteFood(foodForEdit!!.documentId, foodForEdit!!.imgUrl)
                        showPopUp = false
                    },
                    onDismissRequest = {
                        showPopUp = false
                    }
                )
            }
            LaunchedEffect(snackBarMessage) {
                snackBarMessage?.let { message ->
                    scope.launch {
                        val visuals = CustomSnackBarVisuals(
                            message = message.message,
                            actionLabel = null,
                            duration = if (message.isError) SnackbarDuration.Short else SnackbarDuration.Short,
                            withDismissAction = true,
                            isError = message.isError
                        )

                        val result = snackBarHostState.showSnackbar(visuals)

                        // Clear message after dismissal
                        when (result) {
                            SnackbarResult.Dismissed -> foodListViewModel.clearSnackBar()
                            SnackbarResult.ActionPerformed -> foodListViewModel.clearSnackBar()
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun Content1(
    modifier: Modifier = Modifier,
    screenState: FoodListScreenState,
    amountMap: Map<String, String>,
    foodForEdit: (Foods, String) -> Unit,
    onAmountChange: (String, String) -> Unit,
    onAvailabilityChange: (Boolean, String) -> Unit,
    onFoodInListClicked: () -> Unit,
    onSearchButtonClicked: () -> Unit
) {
    val scrollState = rememberScrollState()
    val headerText = "Our Foods"

    Column(
        modifier = modifier
            .verticalScroll(scrollState)
            .background(color = MaterialTheme.colorScheme.background)
            .fillMaxSize()
            .padding(horizontal = 18.dp)
            .padding(top = 10.dp)
            .padding(bottom = 24.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        SectionHeader(
            text = headerText,
            onSearchButtonClicked = onSearchButtonClicked
        )
        when (screenState.foodsList) {
            is Resources.Loading -> {
                CircularProgressIndicator(
                    modifier = modifier
                        .fillMaxSize()
                        .wrapContentSize(align = Alignment.Center),
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
            is Resources.Success -> {
                FoodsColumn(
                    data = screenState.foodsList.data ?: emptyList(),
                    foodForEdit = foodForEdit,
                    amountMap = amountMap,
                    onFoodInListClicked = onFoodInListClicked,
                    onAmountChange = onAmountChange,
                    onAvailabilityChange = onAvailabilityChange
                )
            }
            else -> {
                Text(
                    text = screenState.foodsList.throwable?.localizedMessage ?: "Unknown Error",
                    color = Color.Red
                )
            }
        }
    }
}

@Composable
private fun SectionHeader(
    modifier: Modifier = Modifier,
    text: String,
    onSearchButtonClicked: () -> Unit
) {
    Row(
        modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = modifier.weight(1f)
        )
        SearchButton(
            modifier = modifier
                .size(35.dp),
            onSearchButtonClicked = onSearchButtonClicked
        )
    }
}

@Composable
private fun FoodsColumn(
    modifier: Modifier = Modifier,
    data: List<Foods>,
    amountMap: Map<String, String>,
    foodForEdit: (Foods, String) -> Unit,
    onAmountChange: (String, String) -> Unit,
    onAvailabilityChange: (Boolean, String) -> Unit,
    onFoodInListClicked: () -> Unit
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        data.onEach { item ->
            FoodListItem(
                data = item,
                currentValue = amountMap[item.documentId] ?: "",
                onFoodInListClicked = {
                    foodForEdit(item, item.imgUrl)
                    onFoodInListClicked.invoke()
                },
                onValueChange = { newAmount ->
                    onAmountChange(item.documentId, newAmount)
                },
                onAvailabilityChange = {
                    onAvailabilityChange(it, item.documentId)
                }
            )
        }
    }
}

@Composable
fun BoxForAddButton(
    modifier: Modifier = Modifier,
    onAddButtonClicked: () -> Unit
) {
    Box(
        modifier
            .size(100.dp)
            .padding(horizontal = 14.dp)
            .padding(bottom = 20.dp),
        contentAlignment = Alignment.BottomEnd
    ) {
        AddFoodButton(onAddButtonClicked = onAddButtonClicked)
    }
}

@Composable
fun UpdateAmountButton(
    modifier: Modifier = Modifier,
    onUpdateButtonClicked: () -> Unit
) {
    Surface(
        modifier
            .wrapContentSize()
            .clickable(onClick = onUpdateButtonClicked),
        shape = CircleShape,
        color = MaterialTheme.colorScheme.onBackground,
        contentColor = MaterialTheme.colorScheme.background
    ) {
        Icon(
            modifier = modifier
                .size(35.dp)
                .padding(7.dp),
            painter = painterResource(R.drawable.arrow_up),
            contentDescription = null
        )
    }
}

@Composable
fun AddFoodButton(
    modifier: Modifier = Modifier,
    onAddButtonClicked: () -> Unit
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
                .clickable(onClick = onAddButtonClicked)
                .padding(3.dp),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_plus),
                contentDescription = null,
                modifier = modifier.size(14.dp)
            )
        }
    }
}
