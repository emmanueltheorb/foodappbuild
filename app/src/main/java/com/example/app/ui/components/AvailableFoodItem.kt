package com.example.app.ui.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.app.data.FoodItemState
import com.example.app.data.FoodItemsData

@Composable
fun AvailableFoodItem(
    modifier: Modifier = Modifier,
    data: FoodItemState,
    onFoodItemClicked: () -> Unit
) {
        Column(
            verticalArrangement = Arrangement.spacedBy(4.dp),
            modifier = modifier.clickable(onClick = onFoodItemClicked)
        ) {
            FoodImage(
                foodImage = data.img
            )
            ItemDescription(name = data.foodName, price = data.price.toString())
        }
}

//@Preview(showBackground = true, showSystemUi = true)
//@Composable
//fun AvailableFoodItemPreview() {
//    AvailableFoodItem(data = FoodItemsData[0], onFoodItemClicked = {})
//}

@Composable
fun FoodImage(
    modifier: Modifier = Modifier,
    @DrawableRes foodImage: Int
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(250.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Image(
            modifier = modifier.fillMaxSize(),
            painter = painterResource(foodImage),
            contentDescription = null,
            contentScale = ContentScale.Crop
        )
    }
}

@Composable
fun ItemDescription(
    modifier: Modifier = Modifier,
    name: String,
    price: String
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(80.dp)
    ) {
        Text(
            text = name,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = modifier.weight(1f),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Text(
            text = "₦$price",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = modifier.wrapContentWidth(),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}