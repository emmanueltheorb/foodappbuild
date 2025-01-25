package com.example.app.ui.components.sections

import androidx.annotation.DrawableRes
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CardColors
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.app.data.FoodItemState
import com.example.app.R
import com.example.app.data.OptionState
import com.example.app.data.PriceViewModel
import com.example.app.ui.components.dropdownMenu

@Composable
fun OptionsCheck(
    modifier: Modifier = Modifier,
    data: FoodItemState
) {
    if (data.options == null) {
        NullOptions(data = data)
    } else {
        OptionsSection(
            options = data.options
        )
    }
}

@Composable
fun NullOptions(
    data: FoodItemState?
) {
    var options = data
}

@Composable
fun OptionsSection(
    modifier: Modifier = Modifier,
    options: List<OptionState>
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        NotMergedOptions(
            options = options
        )
        MergedOptions(
            options = options
        )
    }
}

@Composable
fun IncrementSection(
    modifier: Modifier = Modifier,
    amount: Int,
    onDecrementClicked: () -> Unit,
    onIncrementClicked: () -> Unit
) {
    Row(
        modifier = modifier.wrapContentWidth(),
        horizontalArrangement = Arrangement.spacedBy(5.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        MyIconButton(
            icon = R.drawable.ic_minus,
            onClicked = onDecrementClicked
        )
        Text(
            text = amount.toString(),
            color = MaterialTheme.colorScheme.onBackground
        )
        MyIconButton(
            icon = R.drawable.ic_plus,
            onClicked = onIncrementClicked
        )
    }
}

@Composable
fun MyIconButton(
    modifier: Modifier = Modifier,
    @DrawableRes icon: Int,
    containerColor: Color = MaterialTheme.colorScheme.surface,
    onClicked: () -> Unit
) {
    Surface(
        modifier = modifier.size(24.dp),
        shape = CircleShape,
        color = containerColor,
        contentColor = MaterialTheme.colorScheme.onSurface
    ) {
        Box(
            modifier = modifier
                .fillMaxSize()
                .clickable(onClick = onClicked),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(icon),
                contentDescription = null
            )
        }
    }
}

@Composable
fun Surface(
    modifier: Modifier = Modifier,
    text: String,
    width: Dp = 110.dp
) {
    OutlinedCard(
        modifier = Modifier.padding(16.dp),
        border = BorderStroke(width = 2.dp, color = MaterialTheme.colorScheme.onBackground),
        colors = CardColors(
            containerColor = MaterialTheme.colorScheme.background,
            contentColor = MaterialTheme.colorScheme.onBackground,
            disabledContentColor = MaterialTheme.colorScheme.onBackground,
            disabledContainerColor = MaterialTheme.colorScheme.background,
        )
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .width(width)
                .height(50.dp)
                .padding(5.dp)
        ) {
            Text(
                text = text,
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

@Composable
fun PriceTextView(
    modifier: Modifier = Modifier,
    price: Int,
    width: Dp = 110.dp
) {
    Surface(
        text = "₦$price",
        width = width
    )
}


@Composable
fun PriceViewForMerged(
    modifier: Modifier = Modifier,
    viewModel: PriceViewModel,
    index: Int,
    priceList: List<Int>,
    price: Int
) {
//    LaunchedEffect(priceList, index) {
//        viewModel.collectPriceFromList(priceList, index)
//    }
    Surface(text = "₦$price")
}

@Composable
fun PriceView(
    modifier: Modifier = Modifier,
    price: Int
) {
    Surface(text = "₦$price")
}

fun createNameList(
    options: List<OptionState>,
    mergeGroupNumber: Int
): MutableList<String> {

    val nameList = mutableListOf<String>()

    for (option in options) {
        if (option.mergeGroup == mergeGroupNumber) {
            nameList.add(option.name)
        }
    }

    return nameList
}

fun createNumberList(
    options: List<OptionState>,
    mergeGroupNumber: Int
): MutableList<Int> {

    val numberList = mutableListOf<Int>()

    for (option in options) {
        if (option.mergeGroup == mergeGroupNumber) {
            numberList.add(option.price)
        }
    }

    return numberList
}

@Composable
fun OptionWithoutAmount(
    modifier: Modifier = Modifier,
    option: OptionState
) {
    var optionState: OptionState = option
    var priceView = optionState.price


    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Surface(text = optionState.name)
        PriceTextView(
            price = priceView
        )
    }
}

@Composable
fun OptionWithAmount(
    modifier: Modifier = Modifier,
    option: OptionState
) {
    var optionState: OptionState = option
    var amountView by remember { mutableIntStateOf(optionState.amount!!) }
    val lowerLimit = optionState.lowerLimit!!
    val upperLimit = optionState.upperLimit!!
    var priceView = optionState.price
    var price0 = priceView * amountView

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Surface(text = optionState.name)
        IncrementSection(
            amount = amountView,
            onDecrementClicked = {
                if (amountView > lowerLimit) {
                    amountView--
                }
            },
            onIncrementClicked = {
                if (amountView < upperLimit) {
                    amountView++
                }
            }
        )
        PriceTextView(
            price = price0
        )
    }
}

@Composable
fun MergedOptions(
    modifier: Modifier = Modifier,
    options: List<OptionState>
) {
    var price01 by remember { mutableIntStateOf(0) }
    var price02 by remember { mutableIntStateOf(0) }
    var mergeNumberCounter by remember { mutableIntStateOf(0) }
    var optionIndex by remember { mutableIntStateOf(-1) }
    for (option in options) {
        optionIndex++
        if (option.mergeGroup == mergeNumberCounter && option.amount == null) {
            MergedOptionsWithoutAmount(
                options = options,
                mergeGroupNumber = mergeNumberCounter
            )
            mergeNumberCounter++
        } else {
            if (option.mergeGroup == mergeNumberCounter) {
                MergedOptionsWithAmount(
                    options = options,
                    mergeGroupNumber = mergeNumberCounter,
                    optionIndex = optionIndex
                )
                mergeNumberCounter++
            }
        }
    }
}

@Composable
fun NotMergedOptions(
    modifier: Modifier = Modifier,
    options: List<OptionState>
) {
    for (option in options) {
        if (option.mergeGroup == null && option.amount == null) {
            OptionWithoutAmount(
                option = option
            )
        } else {
            if (option.mergeGroup == null) {
                OptionWithAmount(
                    option = option
                )
            }
        }
    }
}

@Composable
fun MergedOptionsWithoutAmount(
    modifier: Modifier = Modifier,
    options: List<OptionState>,
    mergeGroupNumber: Int
) {
    val numberList = createNumberList(
        options = options, mergeGroupNumber = mergeGroupNumber
    )

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        var index = dropdownMenu(options = options, mergedGroupNumber = mergeGroupNumber)
        var priceView = numberList[index]
        PriceTextView(
            price = priceView
        )
    }
}

@Composable
fun MergedOptionsWithAmount(
    modifier: Modifier = Modifier,
    options: List<OptionState>,
    mergeGroupNumber: Int,
    optionIndex: Int
) {
    val optionState: OptionState = options[optionIndex]
    var amountView by remember { mutableIntStateOf(optionState.amount!!) }
    val lowerLimit = optionState.lowerLimit!!
    val upperLimit = optionState.upperLimit!!
    val numberList = createNumberList(
        options = options, mergeGroupNumber = mergeGroupNumber
    )

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        var index = dropdownMenu(options = options, mergedGroupNumber = mergeGroupNumber)
        var priceView = numberList[index]
        var price0 = priceView * amountView
        IncrementSection(
            amount = amountView,
            onDecrementClicked = {
                if (amountView > lowerLimit) {
                    amountView--
                }
            },
            onIncrementClicked = {
                if (amountView < upperLimit) {
                    amountView++
                }
            }
        )
        PriceTextView(
            price = price0
        )
    }
}