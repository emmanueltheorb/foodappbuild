package com.example.app.ui.components.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.app.data.AvailableFoodsScreenState
import com.example.app.data.FoodItemState
import com.example.app.data.ReservedFoodsScreenState
import com.example.app.ui.components.AvailableFoodItem
import com.example.app.ui.components.ReservedFoodItem
import com.example.app.ui.components.SearchButton

@Composable
fun ReservedFoodsScreen(
    modifier: Modifier = Modifier,
    screenState: ReservedFoodsScreenState = ReservedFoodsScreenState(),
    onFoodItemClicked: () -> Unit
) {
    val headerText = "Reserve"
    val scrollState = rememberScrollState()

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
            text = headerText
        )
        FoodsListColumn(
            data = screenState.reservedFoodsItemData,
            onFoodItemClicked = onFoodItemClicked
        )
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
    Column (
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        data.onEach { item ->
            ReservedFoodItem(
                data = item,
                onReserveFoodClicked = onFoodItemClicked
            )
        }
    }
}