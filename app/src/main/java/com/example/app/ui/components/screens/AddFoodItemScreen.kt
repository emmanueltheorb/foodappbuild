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
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.app.R
import com.example.app.data.AddFoodViewModel
import com.example.app.ui.components.MyIntTextField
import com.example.app.ui.components.MyNullableTextField
import com.example.app.ui.components.MyTextField
import com.example.app.ui.components.sections.OptionInputSection
import com.example.app.ui.theme.AppTheme

@Composable
fun AddFoodItemScreen(
    modifier: Modifier = Modifier,
    @DrawableRes foodImage: Int,
    addFoodViewModel: AddFoodViewModel = viewModel()
) {
    val foodInput = addFoodViewModel.foodList[addFoodViewModel.foodIndex]

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background)
            .padding(top = 50.dp)
            .padding(horizontal = 18.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        AvailableFoodItemInput(
            foodImage = foodImage,
            foodName = foodInput.foodName,
            foodPrice = foodInput.price
        )
        AvailabilitySectionInput(
            amountAvailable = foodInput.amount
        )
        OptionInputSection(
            modifier.weight(1f)
        )
    }
}

@Composable
private fun AvailableFoodItemInput(
    modifier: Modifier = Modifier,
    @DrawableRes foodImage: Int,
    foodName: MutableState<String>,
    foodPrice: MutableState<Int>
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        FoodImageInput(
            foodImage = foodImage
        )
        ItemDescriptionInput(
            foodName = foodName,
            foodPrice = foodPrice
        )
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
    modifier: Modifier = Modifier,
    foodName: MutableState<String>,
    foodPrice: MutableState<Int>
) {
    Row(
        modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        MyTextField(
            placeholder = "Food name",
            textValue = foodName
        )
        MyIntTextField(
            placeholder = "Price",
            textValue = foodPrice,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
    }
}

@Composable
private fun AvailabilitySectionInput(
    modifier: Modifier = Modifier,
    amountAvailable: MutableState<Int?>
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
        MyNullableTextField(
            placeholder = "amount",
            textValue = amountAvailable.value?.toString() ?: "",
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            onValueChange = {
                if (!it.isEmpty()) {
                    amountAvailable.value = it.toInt()
                } else {
                    amountAvailable.value = null
                }
            }
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun AddFoodItemScreenPreview() {
    AppTheme(darkTheme = true) {
        AddFoodItemScreen(foodImage = R.drawable.pizza)
    }
}