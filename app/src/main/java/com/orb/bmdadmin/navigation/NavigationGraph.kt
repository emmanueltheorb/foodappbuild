package com.orb.bmdadmin.navigation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import androidx.navigation.toRoute
import com.orb.bmdadmin.data.Foods
import com.orb.bmdadmin.data.ReservedFood
import com.orb.bmdadmin.login.LoginScreen
import com.orb.bmdadmin.login.LoginViewModel
import com.orb.bmdadmin.login.OTPVerificationScreen
import com.orb.bmdadmin.login.SignUpScreen
import com.orb.bmdadmin.login.SplashScreen
import com.orb.bmdadmin.ui.components.screens.AddFoodItemScreen
import com.orb.bmdadmin.ui.components.screens.FoodListScreen
import com.orb.bmdadmin.ui.components.screens.FoodOrdersScreen
import com.orb.bmdadmin.ui.components.screens.ProfileScreen
import com.orb.bmdadmin.ui.components.screens.ReservedScreen
import kotlin.reflect.typeOf

@Composable
fun MyNavigationHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val bottomBarRoutes = listOf(
        Dest.FoodListScreen::class.qualifiedName,
        Dest.OrdersScreen::class.qualifiedName,
        Dest.ProfileScreen::class.qualifiedName
    )

    val showBottomBar = currentDestination?.route in bottomBarRoutes

    val (tabIndex, setTabIndex) = remember {
        mutableIntStateOf(0)
    }
    LaunchedEffect(currentDestination) {
        when (currentDestination) {
            Dest.FoodListScreen -> setTabIndex(0)
            Dest.OrdersScreen -> setTabIndex(1)
            Dest.ProfileScreen -> setTabIndex(2)
        }
    }

    Scaffold(
        modifier = modifier.systemBarsPadding(),
        bottomBar = {
            AnimatedVisibility(visible = showBottomBar) {
                Column(
                    modifier = modifier.fillMaxWidth()
                ) {
                    HorizontalDivider()
                    NavigationBar(
                        modifier = modifier.height(45.dp),
                        containerColor = MaterialTheme.colorScheme.background
                    ) {
                        bottomNavIcons.forEachIndexed { index, item ->
                            NavigationBarItem(
                                selected = tabIndex == index,
                                onClick = {
                                    setTabIndex(index)
                                    navController.navigateToBottomBarDest(item.dest)
//                                navController.navigate(item.dest) {
//                                    // Navigate handling for bottom bar tabs
//                                    popUpTo(navController.graph.findStartDestination()) {
//                                        saveState = true
//                                    }
//                                    launchSingleTop = true
//                                    restoreState = true
//                                }
                                },
                                icon = {
                                    BadgedBox(
                                        badge = {
                                            if (item.hasNews) {
                                                Badge(containerColor = Color.Green)
                                            }
                                        }
                                    ) {
                                        Image(
                                            modifier = modifier.size(20.dp),
                                            painter = painterResource(
                                                if (index == tabIndex) {
                                                    item.selectedIcon
                                                } else {
                                                    item.unselectedIcon
                                                }
                                            ),
                                            contentDescription = item.title,
                                            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onBackground)
                                        )
                                    }
                                },
                                colors = NavigationBarItemDefaults.colors(
                                    indicatorColor = MaterialTheme.colorScheme.background
                                )
                            )
                        }
                    }
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = SubGraph.Auth,
            modifier = modifier.padding(innerPadding)
        ) {
            authGraph(navController)
            homeGraph(navController)
        }
    }
}

fun NavGraphBuilder.authGraph(
    navController: NavController
) {
    navigation<SubGraph.Auth>(startDestination = Dest.SplashScreen) {
        composable<Dest.SplashScreen> {
            SplashScreen(
                onNavToHomePage = {
                    navController.navigate(Dest.FoodListScreen) {
                        popUpTo(Dest.SplashScreen) {
                            inclusive = true
                        }
                    }
                },
                onNavToLoginPage = {
                    navController.navigate(Dest.LoginScreen) {
                        popUpTo(Dest.SplashScreen) {
                            inclusive = true
                        }
                    }
                },
                onNavToOtpPage = {
                    navController.navigate(Dest.OtpScreen) {
                        popUpTo(Dest.SplashScreen) {
                            inclusive = true
                        }
                    }
                }
            )
        }

        composable<Dest.LoginScreen> {
            LoginScreen(
                onNavToOtpPage = {
                    navController.navigate(Dest.OtpScreen) {
                        popUpTo(Dest.LoginScreen) {
                            inclusive = true
                        }
                    }
                },
                onNavToHomePage = {
                    navController.navigate(Dest.FoodListScreen) {
                        popUpTo(Dest.LoginScreen) {
                            inclusive = true
                        }
                    }
                },
                onNavToSignUpPage = {
                    navController.navigate(Dest.SignUpScreen) {
                        restoreState = true
                    }
                }
            )
        }

        composable<Dest.SignUpScreen> {
            SignUpScreen(
                onNavToOtpPage = {
                    navController.navigate(Dest.OtpScreen) {
                        popUpTo(Dest.SignUpScreen) {
                            inclusive = true
                        }
                    }
                },
                onNavToHomePage = {
                    navController.navigate(Dest.FoodListScreen) {
                        popUpTo(Dest.SignUpScreen) {
                            inclusive = true
                        }
                    }
                },
                onNavToLoginPage = {
                    navController.navigate(Dest.LoginScreen) {
                        restoreState = true
                    }
                }
            )
        }

        composable<Dest.OtpScreen> {
            OTPVerificationScreen() {
                navController.navigate(Dest.FoodListScreen) {
                    popUpTo(Dest.OtpScreen) {
                        inclusive = true
                    }

                }
            }
        }
    }
}

fun NavGraphBuilder.homeGraph(
    navController: NavController
) {
    navigation<SubGraph.Home>(startDestination = Dest.FoodListScreen) {
        composable<Dest.FoodListScreen> {
            FoodListScreen(
                navToAddFoodScreen = {
                    navController.navigate(Dest.AddFoodScreen(null, ""))
                },
                onEditFoodClicked = { food, string ->
                    navController.navigate(Dest.AddFoodScreen(food, string))
                },
                onSearchButtonClicked = {  }
            )
        }

        composable<Dest.AddFoodScreen>(
            typeMap = mapOf(
                typeOf<Foods?>() to CustomNavType.FoodType
            )
        ) {
            val addScreen: Dest.AddFoodScreen = it.toRoute<Dest.AddFoodScreen>()
            AddFoodItemScreen(
                foodForEdit = addScreen.foodForEdit,
                imgUrl = addScreen.imgUrl,
                onNavigate = {
                    navController.navigateUp()
                }
            )
        }

        composable<Dest.OrdersScreen> {
            FoodOrdersScreen(
                onFoodClicked = { food, imgUrl ->
                    navController.navigate(Dest.ReservedScreen(food, imgUrl))
                }
            )
        }

        composable<Dest.ReservedScreen>(
            typeMap = mapOf(
                typeOf<ReservedFood>() to CustomNavType2.FoodType
            )
        ) {
            val reservedScreen: Dest.ReservedScreen = it.toRoute<Dest.ReservedScreen>()
            ReservedScreen(
                food = reservedScreen.food,
                imgUrl = reservedScreen.imgUrl
            )
        }

        composable<Dest.ProfileScreen> {
            ProfileScreen(
                navToMessages = {}
            )
        }
    }
}

fun NavController.navigateToBottomBarDest(dest: Dest) {
    val currentRoute = currentBackStackEntry?.destination?.route

    val destRoute = when (dest) {
        is Dest.FoodListScreen -> Dest.FoodListScreen::class.qualifiedName
        is Dest.OrdersScreen -> Dest.OrdersScreen::class.qualifiedName
        is Dest.ProfileScreen -> Dest.ProfileScreen::class.qualifiedName
        else -> throw IllegalArgumentException("Invalid bottom bar destination")
    }
    if (currentRoute != destRoute) {
        navigate(dest) {
            launchSingleTop = true
            restoreState = true
            popUpTo(graph.startDestinationId) {
                saveState = true
            }
        }
    }
}