package com.orb.bmdadmin

import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.core.view.WindowCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.orb.bmdadmin.login.LoginViewModel
import com.orb.bmdadmin.navigation.MyNavigationHost
import com.orb.bmdadmin.navigation.Navigation
import com.orb.bmdadmin.ui.components.screens.AddFoodItemScreen
import com.orb.bmdadmin.ui.components.screens.AvailableFoodsScreen
import com.orb.bmdadmin.ui.components.screens.FoodListScreen
import com.orb.bmdadmin.ui.components.screens.ReservingScreen
import com.orb.bmdadmin.ui.theme.AppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)

        window.statusBarColor = Color.Transparent.toArgb()
        window.navigationBarColor = Color.Transparent.toArgb()

        WindowCompat.getInsetsController(window, window.decorView).apply {
            isAppearanceLightStatusBars = true
            isAppearanceLightNavigationBars = true
        }
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
                    ReservingScreen()
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
//                MyNavigationHost(loginViewModel = loginViewModel)
            }
        }
    }
}
