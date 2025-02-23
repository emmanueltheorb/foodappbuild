package com.orb.bmdadmin.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import androidx.navigation.toRoute
import com.orb.bmdadmin.data.Foods
import com.orb.bmdadmin.login.LoginScreen
import com.orb.bmdadmin.login.LoginViewModel
import com.orb.bmdadmin.login.OTPVerificationScreen
import com.orb.bmdadmin.login.SignUpScreen
import com.orb.bmdadmin.login.SplashScreen
import com.orb.bmdadmin.ui.components.screens.AddFoodItemScreen
import com.orb.bmdadmin.ui.components.screens.FoodListScreen
import kotlin.reflect.typeOf

@Composable
fun MyNavigationHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    loginViewModel: LoginViewModel
) {
    NavHost(navController = navController, startDestination = SubGraph.Auth) {
        auth1Graph(navController, loginViewModel)
        home1Graph(navController)
    }
}

fun NavGraphBuilder.auth1Graph(
    navController: NavController,
    loginViewModel: LoginViewModel
) {
    navigation<SubGraph.Auth>(startDestination = Dest.SplashScreen) {
        composable<Dest.SplashScreen> {
            SplashScreen(
                loginViewModel = loginViewModel,
                onNavToHomePage = {
                    navController.navigate(Dest.FoodListScreen) {
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                onNavToLoginPage = {
                    navController.navigate(Dest.LoginScreen) {
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }

        composable<Dest.LoginScreen> {
            LoginScreen(
                loginViewModel = loginViewModel,
                onNavToOtpPage = {
                    navController.navigate(Dest.OtpScreen) {
                        restoreState = true
                    }
                },
                onNavToHomePage = {
                    navController.navigate(Dest.FoodListScreen) {
                        restoreState = true
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
                loginViewModel = loginViewModel,
                onNavToOtpPage = {
                    navController.navigate(Dest.OtpScreen) {
                        restoreState = true
                    }
                },
                onNavToHomePage = {
                    navController.navigate(Dest.FoodListScreen) {
                        restoreState = true
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
            OTPVerificationScreen(
                loginViewModel = loginViewModel
            ) {
                navController.navigate(Dest.FoodListScreen) {
                    restoreState = true
                }
            }
        }
    }
}

fun NavGraphBuilder.home1Graph(
    navController: NavController
) {
    navigation<SubGraph.Home>(startDestination = Dest.FoodListScreen) {
        composable<Dest.FoodListScreen> {
            FoodListScreen(
                navToAddFoodScreen = {
                    navController.navigate(Dest.AddFoodScreen(null))
                },
                onEditFoodClicked = { food ->
                    navController.navigate(Dest.AddFoodScreen(food))
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
                onNavigate = {
                    navController.navigateUp()
                }
            )
        }
    }
}