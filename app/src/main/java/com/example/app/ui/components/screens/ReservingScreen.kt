package com.example.app.ui.components.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.app.data.PriceViewModel
import com.example.app.data.ReservedScreenState
import com.example.app.data.ReservingScreenState
import com.example.app.ui.components.AvailableFoodItem
import com.example.app.ui.components.QuantityBottomBar
import com.example.app.ui.components.sections.AvailabilitySection
import com.example.app.ui.components.sections.OptionsCheck
import com.example.app.ui.theme.AppTheme

@Composable
fun ReservingScreen(
    modifier: Modifier = Modifier,
    screenState: ReservingScreenState = ReservingScreenState()
) {
    val data = screenState.foodsList[screenState.foodItemId]
    val foodAmount: Int? by remember { mutableStateOf(data.amount) }
    var amount by remember { mutableIntStateOf(1) }
    amount = if (foodAmount == null) {
        5
    } else {
        foodAmount!!
    }
    val sum by remember { mutableIntStateOf(0) }

    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.BottomCenter
    ) {
        ScreenContent(
            modifier = modifier
                .fillMaxSize(),
            screenState = screenState
        )
        QuantityBottomBar(
            modifier = modifier.navigationBarsPadding(),
            price = sum,
            itemPrice = data.price,
            upperLimit = amount,
            data = data
        )
    }
}

@Composable
private fun ScreenContent(
    modifier: Modifier = Modifier,
    screenState: ReservingScreenState
) {
    val data = screenState.foodsList[screenState.foodItemId]
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .verticalScroll(scrollState)
            .background(color = MaterialTheme.colorScheme.background)
            .fillMaxSize()
            .padding(top = 50.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        AvailableFoodItem(
            modifier = modifier.padding(horizontal = 18.dp),
            data = data,
            onFoodItemClicked = {}
        )
        AvailabilitySection(
            data = data
        )
        OptionsCheck(
            data = data
        )
        Spacer(
            modifier = modifier
                .padding(bottom = 56.dp)
                .height(8.dp)
                .navigationBarsPadding()
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ReservingScreenPreview() {
    AppTheme {
        ReservingScreen()
    }
}