package com.orb.bmdadmin.navigation

import com.orb.bmdadmin.data.Foods
import kotlinx.serialization.Serializable

@Serializable
object Home

@Serializable
data class AddScreen(
    val foodForEdit: Foods?
)

sealed class SubGraph {

    @Serializable
    data object Auth: SubGraph()

    @Serializable
    data object Home: SubGraph()
}

sealed class Dest {

    @Serializable
    data object SplashScreen: Dest()

    @Serializable
    data object LoginScreen: Dest()

    @Serializable
    data object SignUpScreen: Dest()

    @Serializable
    data object OtpScreen: Dest()

    @Serializable
    data object FoodListScreen: Dest()

    @Serializable
    data class AddFoodScreen(
        val foodForEdit: Foods?
    ): Dest()
}