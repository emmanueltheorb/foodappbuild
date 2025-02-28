package com.orb.bmdadmin.ui.components.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.orb.bmdadmin.R
import com.orb.bmdadmin.data.FoodItemState
import com.orb.bmdadmin.data.FoodListScreenState
import com.orb.bmdadmin.data.FoodListViewModel
import com.orb.bmdadmin.data.Foods
import com.orb.bmdadmin.repository.Resources
import com.orb.bmdadmin.ui.components.FoodListItem
import com.orb.bmdadmin.ui.components.PopUpForMergeAndRemove
import com.orb.bmdadmin.ui.components.SearchButton
import com.orb.bmdadmin.ui.theme.AppTheme
import java.net.URLEncoder

@Composable
fun FoodListScreen(
    modifier: Modifier = Modifier,
    foodListViewModel: FoodListViewModel? = viewModel(),
    navToAddFoodScreen: () -> Unit,
    onEditFoodClicked: (food: Foods, imgUrl: String) -> Unit,
    onSearchButtonClicked: () -> Unit
) {
    val screenState = foodListViewModel?.foodListScreenState ?: FoodListScreenState()
    var showPopUp by remember { mutableStateOf(false) }
    var foodForEdit: Foods? by remember { mutableStateOf(null) }
    var imgUrl by remember { mutableStateOf("") }
    val onAmountChange: (Int?) -> Unit = {

    }
    val onAvailabilityChange: (Boolean) -> Unit = {

    }
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        foodListViewModel?.getFoods()
    }

    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.BottomEnd
    ) {
        Content1(
            modifier.fillMaxSize(),
            screenState = screenState,
            foodForEdit = { food, string ->
                foodForEdit = food
                imgUrl = string
            },
            onFoodInListClicked = {
                showPopUp = true
            },
            onSearchButtonClicked = onSearchButtonClicked,
            onAmountChange = onAmountChange,
            onAvailabilityChange = onAvailabilityChange
        )
        BoxForAddButton(
            onAddButtonClicked = {
                navToAddFoodScreen.invoke()
            }
        )
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
                    foodListViewModel?.deleteFood(foodForEdit!!.documentId, foodForEdit!!.imgUrl)
                    showPopUp = false
                }
            )
        }
    }
}

@Composable
private fun Content1(
    modifier: Modifier = Modifier,
    screenState: FoodListScreenState,
    foodForEdit: (Foods, String) -> Unit,
    onAmountChange: (Int?) -> Unit,
    onAvailabilityChange: (Boolean) -> Unit,
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
            .padding(top = 50.dp)
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
                        .wrapContentSize(align = Alignment.Center)
                )
            }
            is Resources.Success -> {
                FoodsColumn(
                    data = screenState.foodsList.data ?: emptyList(),
                    foodForEdit = foodForEdit,
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
    foodForEdit: (Foods, String) -> Unit,
    onAmountChange: (Int?) -> Unit,
    onAvailabilityChange: (Boolean) -> Unit,
    onFoodInListClicked: () -> Unit
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        data.onEach { item ->
            FoodListItem(
                data = item,
                onFoodInListClicked = {
                    foodForEdit(item, item.imgUrl)
                    onFoodInListClicked.invoke()
                },
                onValueChange = onAmountChange,
                onAvailabilityChange = onAvailabilityChange
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