package com.orb.bmdadmin.ui.components

import com.orb.bmdadmin.R
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.integration.compose.placeholder
import com.orb.bmdadmin.data.FoodItemState
import com.orb.bmdadmin.data.FoodItemsData
import com.orb.bmdadmin.data.Foods
import com.orb.bmdadmin.ui.theme.AppTheme

@Composable
fun FoodListItem(
    modifier: Modifier = Modifier,
    data: Foods,
    currentValue: String,
    onValueChange: (String) -> Unit,
    onFoodInListClicked: () -> Unit,
    onAvailabilityChange: (Boolean) -> Unit
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
        MySmallWidthTextField(
            placeholder = data.amount,
            value = currentValue,
            onValueChange = onValueChange,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
        AvailabilityText(
            availability = data.availability,
            onAvailabilityChange = onAvailabilityChange
        )
    }
}

@Composable
private fun FoodDetail(
    modifier: Modifier = Modifier,
    data: Foods,
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
            FoodInListImage(foodImage = data.imgUrl)
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

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
private fun FoodInListImage(
    modifier: Modifier = Modifier,
    foodImage: String
) {
    GlideImage(
        modifier = modifier.fillMaxSize(),
        model = foodImage,
        contentDescription = null,
        contentScale = ContentScale.Crop,
        loading = placeholder(R.drawable.right_ic),
        failure = placeholder(R.drawable.ic_close)
    )
}

@Composable
private fun AvailabilityText(
    modifier: Modifier = Modifier,
    availability: Boolean,
    onAvailabilityChange: (Boolean) -> Unit
) {
    var availabilityString = if (availability) "Available" else "Unavailable"
    val stringColor = if (availability) Color.Green else Color.DarkGray

    Text(
        modifier = modifier
            .widthIn(130.dp)
            .clickable(onClick = {
                onAvailabilityChange(availability)
            }),
        text = availabilityString,
        color = stringColor,
        style = MaterialTheme.typography.titleMedium,
        textAlign = TextAlign.Center
    )
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun FoodListItemPreview() {
    AppTheme {
        FoodListItem(
            data = Foods(
                foodName = "Egusi and soup",
                price = 450
            ),
            currentValue = "",
            onFoodInListClicked = {},
            onValueChange = {},
            onAvailabilityChange = {}
        )
    }
}