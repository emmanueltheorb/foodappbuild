package com.orb.bmdadmin.login

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    modifier: Modifier = Modifier,
    loginViewModel: LoginViewModel = hiltViewModel(),
    onNavToHomePage: () -> Unit,
    onNavToLoginPage: () -> Unit,
    onNavToOtpPage: () -> Unit
) {
    val loginScreenState = loginViewModel.loginScreenState
    val isError = loginScreenState.loginError != null
    val context = LocalContext.current

    LaunchedEffect(loginViewModel.hasUser) {
        if (loginViewModel.hasUser == true) {
//            loginViewModel.checkIfUserIsAdmin {
//                if (it) {
                    onNavToHomePage()
//                } else {
//                    onNavToOtpPage
//                }
//            }
        } else {
            onNavToLoginPage()
        }
    }

    Box(
        modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        Column {
            if (isError) {
                Text(
                    text = loginScreenState?.loginError ?: "Unknown Error",
                    color = Color.Red
                )
            }
            Text(
                text = "BMD",
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.onBackground
            )
            if (loginScreenState?.isLoading == true) {
                CircularProgressIndicator(
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
        }
    }
}