package com.orb.bmdadmin

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.orb.bmdadmin.login.LoginViewModel
import com.orb.bmdadmin.navigation.MyNavigationHost
import com.orb.bmdadmin.navigation.Navigation
import com.orb.bmdadmin.ui.components.screens.AddFoodItemScreen
import com.orb.bmdadmin.ui.components.screens.FoodListScreen
import com.orb.bmdadmin.ui.components.screens.ReservingScreen
import com.orb.bmdadmin.ui.theme.AppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val loginViewModel = viewModel(modelClass = LoginViewModel::class.java)
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
//                FoodListScreen(
//                    navToAddFoodScreen = {},
//                    onEditFoodClicked = {},
//                    onSearchButtonClicked = {}
//                )
//                AddFoodItemScreen(
//                    onNavigate = {}
//                )
//                DynamicItemListWithInteraction()
//                Navigation(loginViewModel = loginViewModel)
                MyNavigationHost(loginViewModel = loginViewModel)
            }
        }
    }
}
