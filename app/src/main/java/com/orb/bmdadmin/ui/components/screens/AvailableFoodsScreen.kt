package com.orb.bmdadmin.ui.components.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.orb.bmdadmin.data.AvailableFoodsScreenState
import com.orb.bmdadmin.data.FoodItemState
import com.orb.bmdadmin.ui.components.FoodImage
import com.orb.bmdadmin.ui.components.ItemDescription
import com.orb.bmdadmin.ui.components.SearchButton

@Composable
fun AvailableFoodsScreen(
    modifier: Modifier = Modifier,
    screenState: AvailableFoodsScreenState = AvailableFoodsScreenState(),
    onSearchButtonClicked: () -> Unit,
    onFoodItemClicked: () -> Unit
) {
    val headerText = "BMD Foods"

    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.TopEnd
    ) {
        Content(
            headerText = headerText,
            screenState = screenState,
            onFoodItemClicked = onFoodItemClicked
        )
        SearchButtonBox(
            onSearchButtonClicked = onSearchButtonClicked
        )
    }
}

@Composable
fun SearchButtonBox(
    modifier: Modifier = Modifier,
    onSearchButtonClicked: () -> Unit
) {
    Box(
        modifier
            .padding(horizontal = 18.dp)
            .padding(top = 45.dp),
        contentAlignment = Alignment.BottomEnd
    ) {
        SearchButton(onSearchButtonClicked = onSearchButtonClicked)
    }
}

@Composable
private fun SectionHeader(
    modifier: Modifier = Modifier,
    text: String
) {
    Text(
        text = text,
        style = MaterialTheme.typography.headlineLarge,
        color = MaterialTheme.colorScheme.onBackground,
        modifier = modifier.fillMaxWidth()
    )
}

@Composable
private fun FoodsListColumn(
    modifier: Modifier = Modifier,
    data: List<FoodItemState>,
    onFoodItemClicked: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(top = 10.dp)
            .padding(bottom = 40.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        data.onEach { item ->
            AvailableFoodItemForList(
                data = item,
                onFoodItemClicked = onFoodItemClicked
            )
        }
    }
}

@Composable
private fun AvailableFoodItemForList(
    modifier: Modifier = Modifier,
    data: FoodItemState,
    onFoodItemClicked: () -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(4.dp),
        modifier = modifier.clickable(onClick = onFoodItemClicked)
    ) {
        FoodImage(
            modifier.height(220.dp),
            foodImage = data.img
        )
        ItemDescription(name = data.foodName, price = data.price.toString())
    }
}

@Composable
private fun Content(
    modifier: Modifier = Modifier,
    headerText: String,
    screenState: AvailableFoodsScreenState,
    onFoodItemClicked: () -> Unit
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .verticalScroll(scrollState)
            .background(color = MaterialTheme.colorScheme.background)
            .fillMaxSize()
            .padding(horizontal = 18.dp)
            .padding(top = 50.dp)
            .padding(bottom = 10.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        SectionHeader(
            text = headerText
        )
        FoodsListColumn(
            data = screenState.foodsItemData,
            onFoodItemClicked = onFoodItemClicked
        )
    }
}

//@Preview(showBackground = true, showSystemUi = true)
//@Composable
//private fun AvailableFoodsScreenPreview() {
//    AppTheme {
//        AvailableFoodsScreen(onSearchButtonClicked = {}, onFoodItemClicked = {})
//    }
//}