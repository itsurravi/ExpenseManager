package com.ravikantsharma.auth.presentation.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.ravikantsharma.auth.presentation.login.LoginScreen
import com.ravikantsharma.auth.presentation.register.RegisterScreen

fun NavGraphBuilder.authGraph(
    navController: NavHostController
) {
    navigation<AuthBaseRoute>(
        startDestination = LoginRoute
    ) {
        composable<LoginRoute> {
            LoginScreen {
                navController.navigateToRegisterScreen()
            }
        }

        composable<RegisterRoute> {
            RegisterScreen()
        }
    }
}