package com.orb.bmdadmin.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import com.orb.bmdadmin.login.LoginScreen
import com.orb.bmdadmin.login.LoginViewModel
import com.orb.bmdadmin.login.OTPVerificationScreen
import com.orb.bmdadmin.login.SignUpScreen
import com.orb.bmdadmin.ui.components.screens.AddFoodItemScreen
import com.orb.bmdadmin.ui.components.screens.FoodListScreen

enum class LoginRoutes {
    SignUp,
    SignIn,
    Otp
}

enum class HomeRoutes {
    FoodsList,
    AddFood
}

enum class NestedRoutes {
    Main,
    Login
}

@Composable
fun Navigation(
    navController: NavHostController = rememberNavController(),
    loginViewModel: LoginViewModel
) {
    NavHost(
        navController = navController,
        startDestination = NestedRoutes.Login.name
    ) {
        authGraph(navController, loginViewModel)
        homeGraph(navController)
    }
}

fun NavGraphBuilder.authGraph(
    navController: NavController,
    loginViewModel: LoginViewModel
) {
    navigation(
        startDestination = LoginRoutes.SignIn.name,
        route = NestedRoutes.Login.name
        ) {
        composable(route = LoginRoutes.SignIn.name) {
            LoginScreen(
                loginViewModel = loginViewModel,
                onNavToOtpPage = {
                    navController.navigate(LoginRoutes.Otp.name) {
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                onNavToHomePage = {
                    navController.navigate(NestedRoutes.Main.name) {
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                onNavToSignUpPage = {
                    navController.navigate(LoginRoutes.SignUp.name) {
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }

        composable(route = LoginRoutes.SignUp.name) {
            SignUpScreen(
                loginViewModel = loginViewModel,
                onNavToOtpPage = {
                    navController.navigate(LoginRoutes.Otp.name) {
                        restoreState = true
                    }
                },
                onNavToHomePage = {
                    navController.navigate(NestedRoutes.Main.name) {
                        restoreState = true
                    }
                },
                onNavToLoginPage = {
                    navController.navigate(LoginRoutes.SignIn.name)
                }
            )
        }

        composable(route = LoginRoutes.Otp.name) {
            OTPVerificationScreen(
                loginViewModel = loginViewModel
            ) {
                navController.navigate(NestedRoutes.Main.name) {
                    restoreState = true
                }
            }
        }
    }
}

fun NavGraphBuilder.homeGraph(
    navController: NavController
) {
    navigation(
        HomeRoutes.FoodsList.name,
        NestedRoutes.Main.name
    ) {
        composable(HomeRoutes.FoodsList.name) {
            FoodListScreen(
                navToAddFoodScreen = {
                    navController.navigate(HomeRoutes.AddFood.name)
                },
                onEditFoodClicked = { food ->
                    navController.navigate(HomeRoutes.AddFood.name)
                },
                onSearchButtonClicked = {}
            )
        }

        composable(HomeRoutes.AddFood.name) {
            AddFoodItemScreen {
                navController.navigateUp()
            }
        }
    }
}