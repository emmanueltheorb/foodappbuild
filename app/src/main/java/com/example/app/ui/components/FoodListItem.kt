package com.example.app.ui.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.example.app.data.FoodItemState
import com.example.app.data.FoodItemsData
import com.example.app.ui.theme.AppTheme

@Composable
fun FoodListItem(
    modifier: Modifier = Modifier,
    data: FoodItemState,
    onFoodInListClicked: () -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        FoodDetail(
            modifier = modifier.weight(1f),
            data = data,
            onFoodInListClicked = onFoodInListClicked
        )
        Spacer(modifier.width(7.dp))
        AvailabilityText(availability = data.availability)
    }
}

@Composable
private fun FoodDetail(
    modifier: Modifier = Modifier,
    data: FoodItemState,
    onFoodInListClicked: () -> Unit
) {
    Card(
        modifier = modifier
            .height(50.dp)
            .clickable(onClick = onFoodInListClicked),
        shape = RoundedCornerShape(9.dp)
    ) {
        Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            FoodInListImage(foodImage = data.img)
            Surface(
                modifier = modifier
                    .fillMaxSize()
                    .alpha(.3f), color = Color.Black
            ) {}
            Text(
                text = data.foodName,
                style = MaterialTheme.typography.titleLarge,
                color = Color.White
            )
        }
    }
}

@Composable
private fun FoodInListImage(
    modifier: Modifier = Modifier,
    @DrawableRes foodImage: Int
) {
    Image(
        modifier = modifier.fillMaxSize(),
        painter = painterResource(foodImage),
        contentDescription = null,
        contentScale = ContentScale.Crop
    )
}

@Composable
private fun AvailabilityText(
    modifier: Modifier = Modifier,
    availability: Boolean
) {
    var availability by remember { mutableStateOf(availability) }
    var availabilityString by remember { mutableStateOf("") }
    var stringColor by remember { mutableStateOf(Color.Green) }
    if (availability) {
        availabilityString = "Available"
        stringColor = Color.Green
    } else {
        availabilityString = "Unavailable"
        stringColor = Color.DarkGray
    }

    Text(
        modifier = modifier
            .widthIn(130.dp)
            .clickable(onClick = {
                availability = !availability
            }),
        text = availabilityString,
        color = stringColor,
        style = MaterialTheme.typography.titleMedium,
        textAlign = TextAlign.Center
    )
}

//@Preview(showBackground = true, showSystemUi = true)
//@Composable
//private fun FoodListItemPreview() {
//    AppTheme {
//        FoodListItem(data = FoodItemsData[1], onFoodInListClicked = {})
//    }
//}