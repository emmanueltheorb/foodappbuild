package com.example.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.app.ui.components.DynamicItemListWithInteraction
import com.example.app.ui.components.screens.AddFoodItemScreen
import com.example.app.ui.components.screens.ReservingScreen
import com.example.app.ui.components.sections.OptionCheck
import com.example.app.ui.components.sections.OptionInputItem
import com.example.app.ui.theme.AppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppTheme {
//                    AvailableFoodsScreen(
//                        headerText = "BMD Foods",
//                        onSearchButtonClicked = {},
//                        onFoodItemClicked = {}
//                    )
//                    OptionsSection(
//                        data = FoodItemsData[3],
//                        onDecrementClicked = {},
//                        onIncrementClicked = {}
//                    )
//                    ReservingScreen()
                AddFoodItemScreen(
                    modifier = Modifier.fillMaxSize(),
                    foodImage = R.drawable.pizza
                )
//                DynamicItemListWithInteraction()
            }
        }
    }
}
