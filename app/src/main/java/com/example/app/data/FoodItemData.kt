package com.example.app.data

import androidx.annotation.DrawableRes
import androidx.compose.runtime.mutableStateOf
import com.example.app.R

data class FoodItemState(
    val id: Int,
    @DrawableRes val img: Int,
    val foodName: String,
    val price: Int,
    val availability: Boolean = true,
    val amount: Int?,
    val options: MutableList<OptionState>?
)

data class OptionState(
    val id: Int,
    val name: String,
    val price: Int,
    val amount: Int?,
    val upperLimit: Int?,
    val lowerLimit: Int?,
    val mergeGroup: Int? = null,
    val mergeId: Int? = null
)

data class OptionReserved(
    val id: Int,
    val name: String,
    val amount: Int?,
    val price: Int
)


val FoodItemsData = mutableListOf(
    FoodItemState(
        id = 0,
        img = R.drawable.pizza,
        foodName = "Pizza",
        price = 2000,
        amount = 15,
        options = null
    ),
    FoodItemState(
        id = 1,
        img = R.drawable.eba_and_soup,
        foodName = "Eba and soup",
        price = 2500,
        amount = null,
        options = mutableListOf(
            OptionState(
                id = 0,
                name = "Egusi soup",
                price = 400,
                amount = null,
                upperLimit = null,
                lowerLimit = null,
                mergeGroup = 0,
                mergeId = 0
            ),
            OptionState(
                id = 1,
                name = "Banga soup",
                price = 400,
                amount = null,
                upperLimit = null,
                lowerLimit = null,
                mergeGroup = 0,
                mergeId = 1
            ),
            OptionState(
                id = 2,
                name = "Vegetable soup",
                price = 600,
                amount = null,
                upperLimit = null,
                lowerLimit = null,
                mergeGroup = 0,
                mergeId = 2
            ),
            OptionState(
                id = 3,
                name = "Eba",
                price = 300,
                amount = 1,
                upperLimit = 5,
                lowerLimit = 1,
                mergeGroup = 1,
                mergeId = 0
            ),
            OptionState(
                id = 4,
                name = "Fufu",
                price = 300,
                amount = 1,
                upperLimit = 5,
                lowerLimit = 1,
                mergeGroup = 1,
                mergeId = 1
            ),
            OptionState(
                id = 5,
                name = "Turkey",
                price = 1600,
                amount = 1,
                upperLimit = 2,
                lowerLimit = 1,
                mergeGroup = 2,
                mergeId = 0
            ),
            OptionState(
                id = 6,
                name = "Chicken",
                price = 1400,
                amount = 1,
                upperLimit = 2,
                lowerLimit = 1,
                mergeGroup = 2,
                mergeId = 1
            ),
            OptionState(
                id = 7,
                name = "Goat meat",
                price = 1500,
                amount = 1,
                upperLimit = 2,
                lowerLimit = 1,
                mergeGroup = 2,
                mergeId = 2
            ),
            OptionState(
                id = 8,
                name = "Kpomo",
                price = 500,
                amount = 1,
                upperLimit = 3,
                lowerLimit = 1,
                mergeGroup = 2,
                mergeId = 3
            ),
            OptionState(
                id = 9,
                name = "Fish",
                price = 900,
                amount = 1,
                upperLimit = 2,
                lowerLimit = 1,
                mergeGroup = 2,
                mergeId = 4
            )
        )
    ),
    FoodItemState(
        id = 2,
        img = R.drawable.meat_pie,
        foodName = "Meat pie",
        price = 1500,
        amount = 6,
        options = null
    ),
    FoodItemState(
        id = 3,
        img = R.drawable.eba_and_stew,
        foodName = "Eba and stew",
        price = 2500,
        amount = null,
        options = mutableListOf(
            OptionState(
                id = 0,
                name = "Stew",
                price = 400,
                amount = null,
                upperLimit = null,
                lowerLimit = null
            ),
            OptionState(
                id = 1,
                name = "Eba",
                price = 300,
                amount = 1,
                upperLimit = 5,
                lowerLimit = 1,
                mergeGroup = 0,
                mergeId = 0
            ),
            OptionState(
                id = 2,
                name = "Fufu",
                price = 300,
                amount = 1,
                upperLimit = 5,
                lowerLimit = 1,
                mergeGroup = 0,
                mergeId = 1
            ),
            OptionState(
                id = 3,
                name = "Turkey",
                price = 1600,
                amount = 1,
                upperLimit = 2,
                lowerLimit = 1,
                mergeGroup = 1,
                mergeId = 0
            ),
            OptionState(
                id = 4,
                name = "Chicken",
                price = 1400,
                amount = 1,
                upperLimit = 2,
                lowerLimit = 1,
                mergeGroup = 1,
                mergeId = 1
            ),
            OptionState(
                id = 5,
                name = "Goat meat",
                price = 1500,
                amount = 1,
                upperLimit = 2,
                lowerLimit = 1,
                mergeGroup = 1,
                mergeId = 2
            ),
            OptionState(
                id = 6,
                name = "Kpomo",
                price = 500,
                amount = 1,
                upperLimit = 3,
                lowerLimit = 1,
                mergeGroup = 1,
                mergeId = 3
            ),
            OptionState(
                id = 7,
                name = "Fish",
                price = 900,
                amount = 1,
                upperLimit = 2,
                lowerLimit = 1,
                mergeGroup = 1,
                mergeId = 4
            ),
            OptionState(
                id = 8,
                name = "Test Fish",
                price = 300,
                amount = 1,
                upperLimit = 2,
                lowerLimit = 1
            )
        )
    ),
    FoodItemState(
        id = 4,
        img = R.drawable.hot_dogs,
        foodName = "Hot dogs",
        price = 900,
        amount = 4,
        options = null
    ),
    FoodItemState(
        id = 5,
        img = R.drawable.ice_cream_plate,
        foodName = "Ice cream",
        price = 1500,
        amount = 25,
        options = mutableListOf(
            OptionState(
                id = 0,
                name = "Ice cream plate",
                price = 1500,
                amount = 1,
                upperLimit = null,//6
                lowerLimit = null,//1
                mergeGroup = 0,
                mergeId = 0
            ),
            OptionState(
                id = 1,
                name = "Ice cream medium cup",
                price = 1000,
                amount = 1,
                upperLimit = null,//6
                lowerLimit = null,//1
                mergeGroup = 0,
                mergeId = 1
            ),
            OptionState(
                id = 2,
                name = "Ice cream small cup",
                price = 700,
                amount = 1,
                upperLimit = null,//6
                lowerLimit = null,//1
                mergeGroup = 0,
                mergeId = 2
            ),
            OptionState(
                id = 3,
                name = "Chocolate Flavor",
                price = 0,
                amount = null,
                upperLimit = null,
                lowerLimit = null,
                mergeGroup = 1,
                mergeId = 0
            ),
            OptionState(
                id = 4,
                name = "Vanilla Flavor",
                price = 0,
                amount = null,
                upperLimit = null,
                lowerLimit = null,
                mergeGroup = 1,
                mergeId = 1
            ),
            OptionState(
                id = 5,
                name = "StrawBerry Flavor",
                price = 0,
                amount = null,
                upperLimit = null,
                lowerLimit = null,
                mergeGroup = 1,
                mergeId = 2
            ),
            OptionState(
                id = 6,
                name = "Banana Flavor",
                price = 0,
                amount = null,
                upperLimit = null,
                lowerLimit = null,
                mergeGroup = 1,
                mergeId = 3
            )
        )
    )
)