package com.example.app.ui.components.screens

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.app.R
import com.example.app.ui.components.MyTextField
import com.example.app.ui.components.sections.OptionInputSection
import com.example.app.ui.theme.AppTheme

@Composable
fun AddFoodItemScreen(
    modifier: Modifier = Modifier,
    @DrawableRes foodImage: Int
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background)
            .padding(top = 50.dp)
            .padding(horizontal = 18.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        AvailableFoodItemInput(foodImage = foodImage)
        AvailabilitySectionInput()
        OptionInputSection(
            modifier.weight(1f)
        )
    }
}

@Composable
private fun AvailableFoodItemInput(
    modifier: Modifier = Modifier,
    @DrawableRes foodImage: Int
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        FoodImageInput(
            foodImage = foodImage
        )
        ItemDescriptionInput()
    }
}

@Composable
private fun FoodImageInput(
    modifier: Modifier = Modifier,
    @DrawableRes foodImage: Int
) {
    val onFoodItemClicked = {}

    Card(
        modifier = modifier
            .clickable(onClick = onFoodItemClicked)
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
private fun ItemDescriptionInput(
    modifier: Modifier = Modifier
) {
    Row(
        modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        MyTextField(placeholder = "Food name")
        MyTextField(
            placeholder = "Price",
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
    }
}

@Composable
private fun AvailabilitySectionInput(
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Text(
            text = "Available: ",
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.bodyMedium
        )
        MyTextField(placeholder = "amount")
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun AddFoodItemScreenPreview() {
    AppTheme(darkTheme = true) {
        AddFoodItemScreen(foodImage = R.drawable.pizza)
    }
}