package com.orb.bmdadmin

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import com.orb.bmdadmin.ui.components.screens.AddFoodItemScreen
import com.orb.bmdadmin.ui.theme.AppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppTheme {
//                    AvailableFoodsScreen(
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
                    modifier = Modifier.fillMaxSize()
                )
//                DynamicItemListWithInteraction()
            }
        }
    }
}
