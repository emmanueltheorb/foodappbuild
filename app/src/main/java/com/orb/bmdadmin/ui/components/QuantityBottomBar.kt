package com.orb.bmdadmin.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.orb.bmdadmin.data.FoodItemState
import com.orb.bmdadmin.ui.components.sections.IncrementSection

@Composable
fun QuantityBottomBar(
    modifier: Modifier = Modifier,
    price: Int,
    itemPrice: Int,
    upperLimit: Int,
    data: FoodItemState
) {
    var quantity by remember { mutableIntStateOf(1) }
    var priceView by remember { mutableIntStateOf(itemPrice) }
    if (price != 0) {
        priceView = price
    }
    var newPrice = priceView * quantity

    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        HorizontalDivider()
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier
                .navigationBarsPadding()
                .then(modifier.padding(horizontal = 18.dp))
                .heightIn(min = 56.dp)
        ) {
            Text(
                text = "Quantity",
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = modifier.width(10.dp))
            IncrementSection(
                amount = quantity,
                onDecrementClicked = {
                    if (quantity > 1) {
                        quantity--
                    }
                },
                onIncrementClicked = {
                    if (quantity < upperLimit) {
                        quantity++
                    }
                }
            )
            Spacer(modifier = modifier.width(10.dp))
            Text(
                text = "₦$newPrice",
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = modifier.width(15.dp))
            Button(
                modifier = modifier.weight(1f),
                colors = ButtonColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    contentColor = MaterialTheme.colorScheme.onSurface,
                    disabledContainerColor = MaterialTheme.colorScheme.surface,
                    disabledContentColor = MaterialTheme.colorScheme.onSurface
                ),
                contentPadding = PaddingValues(horizontal = 4.dp),
                onClick = {
//                    val reservedItem: ReservedScreenState = ReservedScreenState(
//                        id = data.id,
//                        img = data.img,
//                        name = data.foodName,
//                        price = newPrice,
//                        options = data.options,
//                        startClock = true
//                    )
//
//                    ReservedItemData.add(reservedItem)
                }
            ) {
                Text(
                    text = "RESERVE",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    maxLines = 1
                )
            }
        }
    }
}