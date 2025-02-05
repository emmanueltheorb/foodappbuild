package com.orb.bmdadmin.ui.components.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.orb.bmdadmin.data.FoodItemState
import com.orb.bmdadmin.data.OptionReserved
import com.orb.bmdadmin.ui.components.FoodImage
import com.orb.bmdadmin.ui.components.ItemDescription
import com.orb.bmdadmin.ui.components.ReservedBottomBar
import com.orb.bmdadmin.ui.components.sections.NullOptions
import com.orb.bmdadmin.ui.components.sections.PriceView
import com.orb.bmdadmin.ui.components.sections.Surface

@Composable
fun ReservedScreen(
    modifier: Modifier = Modifier,
    data: FoodItemState,
    price: Int,
    quantity: Int,
    optionsReserved: MutableList<OptionReserved>
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.BottomCenter
    ) {
        ScreenContent(
            modifier = modifier.fillMaxSize(),
            data = data, price =  price, optionsReserved = optionsReserved
        )
        ReservedBottomBar(
            modifier = modifier.navigationBarsPadding(),
            quantity = quantity
        )
    }
}

@Composable
private fun ScreenContent(
    modifier: Modifier = Modifier,
    data: FoodItemState,
    price: Int,
    optionsReserved: MutableList<OptionReserved>
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .verticalScroll(scrollState)
            .background(color = MaterialTheme.colorScheme.background)
            .fillMaxSize()
            .padding(top = 50.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        AvailableFoodItemReserved(data = data, price = price)
        OptionsCheck(data = data, optionsReserved = optionsReserved)
        Spacer(
            modifier = modifier
                .padding(bottom = 56.dp)
                .height(8.dp)
                .navigationBarsPadding()
        )
    }
}

@Composable
private fun AvailableFoodItemReserved(
    modifier: Modifier = Modifier,
    data: FoodItemState,
    price: Int
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        FoodImage(
            foodImage = data.img
        )
        ItemDescription(name = data.foodName, price = price.toString())
    }
}

@Composable
private fun OptionsCheck(
    modifier: Modifier = Modifier,
    data: FoodItemState,
    optionsReserved: MutableList<OptionReserved>
) {
    if (data.options == null) {
        NullOptions(data = data)
    } else {
        Options(
            optionsReserved = optionsReserved
        )
    }
}

@Composable
private fun Options(
    modifier: Modifier = Modifier,
    optionsReserved: MutableList<OptionReserved>
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ReservedOptions(optionsReserved = optionsReserved)
    }
}

@Composable
private fun ReservedOptions(
    modifier: Modifier = Modifier,
    optionsReserved: MutableList<OptionReserved>
) {
    for (option in optionsReserved) {
        if (option.amount == null) {
            OptionWithoutAmount(name = option.name, price = option.price)
        } else {
            OptionWithAmount(name = option.name, amount = option.amount, price = option.price)
        }
    }
}

@Composable
private fun OptionWithoutAmount(
    modifier: Modifier = Modifier,
    name: String,
    price: Int
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Surface(text = name)
        PriceView(price = price)
    }
}

@Composable
private fun OptionWithAmount(
    modifier: Modifier = Modifier,
    name: String,
    amount: Int,
    price: Int
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Surface(text = name)
        Text(
            text = amount.toString(),
            color = MaterialTheme.colorScheme.onBackground
        )
        PriceView(price = price)
    }
}