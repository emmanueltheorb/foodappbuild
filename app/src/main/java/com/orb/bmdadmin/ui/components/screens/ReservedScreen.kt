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
import androidx.compose.foundation.layout.systemBarsPadding
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
import com.orb.bmdadmin.data.ReservedFood
import com.orb.bmdadmin.ui.components.FoodImage
import com.orb.bmdadmin.ui.components.FoodImageUrl
import com.orb.bmdadmin.ui.components.ItemDescription
import com.orb.bmdadmin.ui.components.ReservedBottomBar
import com.orb.bmdadmin.ui.components.sections.NullOptions
import com.orb.bmdadmin.ui.components.sections.PriceView
import com.orb.bmdadmin.ui.components.sections.Surface
import kotlin.code

@Composable
fun ReservedScreen(
    modifier: Modifier = Modifier,
    food: ReservedFood,
    imgUrl: String
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .systemBarsPadding(),
        contentAlignment = Alignment.BottomCenter
    ) {
        ScreenContent(
            modifier = modifier.fillMaxSize(),
            food = food.copy(imgUrl = imgUrl)
        )
        ReservedBottomBar(
            quantity = food.quantity,
            address = food.address,
            phoneNumber = food.phoneNumber,
            code = food.code
        )
    }
}

@Composable
private fun ScreenContent(
    modifier: Modifier = Modifier,
    food: ReservedFood
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .verticalScroll(scrollState)
            .background(color = MaterialTheme.colorScheme.background)
            .fillMaxSize()
            .padding(top = 10.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        AvailableFoodItemReserved(food = food)
        Options(optionsReserved = food.options)
        Spacer(
            modifier = modifier
                .padding(bottom = 106.dp)
                .height(8.dp)
        )
    }
}

@Composable
private fun AvailableFoodItemReserved(
    modifier: Modifier = Modifier,
    food: ReservedFood
) {
    Column(
        modifier = modifier.padding(horizontal = 18.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        FoodImageUrl(
            foodImage = food.imgUrl
        )
        ItemDescription(name = food.name, price = food.price.toString())
    }
}

@Composable
private fun Options(
    modifier: Modifier = Modifier,
    optionsReserved: List<OptionReserved>
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
    optionsReserved: List<OptionReserved>
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