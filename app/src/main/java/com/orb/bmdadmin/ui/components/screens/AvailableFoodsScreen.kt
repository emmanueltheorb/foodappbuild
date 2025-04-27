package com.orb.bmdadmin.ui.components.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.orb.bmdadmin.data.AvailableFoodsScreenState
import com.orb.bmdadmin.data.FoodItemState
import com.orb.bmdadmin.ui.components.FoodImage
import com.orb.bmdadmin.ui.components.ItemDescription
import com.orb.bmdadmin.ui.components.SearchButton
import com.orb.bmdadmin.ui.theme.AppTheme

@Composable
fun AvailableFoodsScreen(
    modifier: Modifier = Modifier,
    screenState: AvailableFoodsScreenState = AvailableFoodsScreenState(),
    onSearchButtonClicked: () -> Unit,
    onFoodItemClicked: () -> Unit
) {
    val headerText = "BMD Foods"
    val lazyListState = rememberLazyListState()
    val scrollThreshold = 150

    val isScrolledBeyondThreshold by remember {
        derivedStateOf {
            lazyListState.firstVisibleItemIndex == 0 &&
                    lazyListState.firstVisibleItemScrollOffset <= scrollThreshold
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background)
            .systemBarsPadding(),
        contentAlignment = Alignment.TopEnd
    ) {
        FoodsListColumn1(
            modifier = modifier
                .background(color = MaterialTheme.colorScheme.background)
                .fillMaxSize()
                .padding(horizontal = 18.dp),
            data = screenState.foodsItemData,
            lazyListState = lazyListState,
            onFoodItemClicked = onFoodItemClicked
        )
        AnimatedVisibility(
            visible = isScrolledBeyondThreshold,
            exit = slideOutVertically(),
            enter = slideInVertically()
        ) {
            SectionHeader(
                modifier = modifier
                    .background(color = MaterialTheme.colorScheme.background)
                    .padding(horizontal = 18.dp)
                    .padding(top = 2.5.dp),
                text = headerText
            )
        }
        AnimatedVisibility(
            visible = isScrolledBeyondThreshold,
            exit = slideOutVertically(),
            enter = slideInVertically()
        ) {
            SearchButtonBox(
                onSearchButtonClicked = onSearchButtonClicked
            )
        }
    }
}

@Composable
fun SearchButtonBox(
    modifier: Modifier = Modifier,
    onSearchButtonClicked: () -> Unit
) {
    Box(
        modifier
            .padding(horizontal = 18.dp),
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
private fun FoodsListColumn1(
    modifier: Modifier = Modifier,
    lazyListState: LazyListState,
    data: List<FoodItemState>,
    onFoodItemClicked: () -> Unit
) {
    LazyColumn(
        state = lazyListState,
        modifier = modifier
            .height(900.dp),
        contentPadding = PaddingValues(bottom = 10.dp, top = 50.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        items(data) { item ->
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
    lazyListState: LazyListState,
    screenState: AvailableFoodsScreenState,
    onFoodItemClicked: () -> Unit
) {
    Column(
        modifier = modifier
            .background(color = MaterialTheme.colorScheme.background)
            .fillMaxSize()
            .padding(horizontal = 18.dp)
            .padding(top = 50.dp)
            .padding(bottom = 10.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        SectionHeader(
            modifier = modifier
                .padding(horizontal = 18.dp)
                .padding(top = 50.dp),
            text = headerText
        )
        FoodsListColumn1(
            modifier = modifier
                .background(color = MaterialTheme.colorScheme.background)
                .fillMaxSize()
                .padding(horizontal = 18.dp)
                .padding(top = 80.dp)
                .padding(bottom = 10.dp),
            data = screenState.foodsItemData,
            lazyListState = lazyListState,
            onFoodItemClicked = onFoodItemClicked
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun AvailableFoodsScreenPreview() {
    AppTheme {
        AvailableFoodsScreen(onSearchButtonClicked = {}, onFoodItemClicked = {})
    }
}