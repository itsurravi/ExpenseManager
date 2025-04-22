package com.ravikantsharma.auth.presentation.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.ravikantsharma.auth.presentation.login.LoginScreenRoot
import com.ravikantsharma.auth.presentation.register.RegisterScreenRoot

fun NavGraphBuilder.authGraph(
    navController: NavHostController
) {
    navigation<AuthBaseRoute>(
        startDestination = LoginRoute
    ) {
        composable<LoginRoute> {
            LoginScreenRoot(
                onRegisterClick = {
                    navController.navigate(RegisterRoute)
                }
            )
        }

        composable<RegisterRoute> {
            RegisterScreenRoot()
        }
    }
}