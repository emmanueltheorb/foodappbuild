package com.example.app.ui.components.screens

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.app.R
import com.example.app.data.AvailableFoodsScreenState
import com.example.app.data.FoodItemState
import com.example.app.data.FoodListScreenState
import com.example.app.ui.components.FoodListItem
import com.example.app.ui.components.ReservedFoodItem
import com.example.app.ui.components.SearchButton
import com.example.app.ui.theme.AppTheme

@Composable
fun FoodListScreen(
    modifier: Modifier = Modifier,
    screenState: FoodListScreenState,
    onFoodInListClicked: () -> Unit,
    onSearchButtonClicked: () -> Unit,
    onAddButtonClicked: () -> Unit
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.BottomEnd
    ) {
        Content(
            modifier.fillMaxSize(),
            screenState = screenState,
            onFoodInListClicked = onFoodInListClicked,
            onSearchButtonClicked = onSearchButtonClicked
        )
        BoxForAddButton(onAddButtonClicked = onAddButtonClicked)
    }
}

@Composable
private fun Content(
    modifier: Modifier = Modifier,
    screenState: FoodListScreenState,
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
        FoodsColumn(
            data = screenState.foodsItemData,
            onFoodInListClicked = onFoodInListClicked
        )
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
    data: List<FoodItemState>,
    onFoodInListClicked: () -> Unit
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        data.onEach { item ->
            FoodListItem(
                data = item,
                onFoodInListClicked = onFoodInListClicked
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

//@Preview(showBackground = true, showSystemUi = true)
//@Composable
//fun FoodListScreenPreview() {
//    AppTheme {
//        FoodListScreen(
//            screenState = FoodListScreenState(),
//            onFoodInListClicked = {},
//            onSearchButtonClicked = {},
//            onAddButtonClicked = {})
//    }
//}