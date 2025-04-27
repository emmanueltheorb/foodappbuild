package com.orb.bmdadmin

import android.content.res.Configuration
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
import com.orb.bmdadmin.ui.components.screens.AddFoodItemScreen
import com.orb.bmdadmin.ui.components.screens.AvailableFoodsScreen
import com.orb.bmdadmin.ui.components.screens.FoodListScreen
import com.orb.bmdadmin.ui.components.screens.ReservingScreen
import com.orb.bmdadmin.ui.components.SwipeToDeleteList
import com.orb.bmdadmin.ui.components.screens.FoodOrdersScreen
import com.orb.bmdadmin.ui.theme.AppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)

        window.statusBarColor = Color.Transparent.toArgb()
        window.navigationBarColor = Color.Transparent.toArgb()

        val isDarkMode = resources.configuration.uiMode and
                Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES

        WindowCompat.getInsetsController(window, window.decorView).apply {
            isAppearanceLightStatusBars = !isDarkMode
            isAppearanceLightNavigationBars = !isDarkMode
        }
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
//                FoodListScreen(
//                    navToAddFoodScreen = {},
//                    onEditFoodClicked = { f, s ->
//
//                    },
//                    onSearchButtonClicked = {}
//                )
//                AddFoodItemScreen(
//                    onNavigate = {}
//                )
//                DynamicItemListWithInteraction()
                MyNavigationHost()
//                SwipeToDeleteList()
//                FoodOrdersScreen { f, s ->
//
//                }
            }
        }
    }
}
