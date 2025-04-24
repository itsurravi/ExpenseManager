package com.ravikantsharma.auth.presentation.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.ravikantsharma.auth.presentation.create_pin.ConfirmPinScreenRoot
import com.ravikantsharma.auth.presentation.create_pin.CreatePinScreenRoot
import com.ravikantsharma.auth.presentation.login.LoginScreenRoot
import com.ravikantsharma.auth.presentation.navigation.model.CreatePinData
import com.ravikantsharma.auth.presentation.register.RegisterScreenRoot
import com.ravikantsharma.ui.SerializableNavType
import kotlinx.serialization.serializer
import kotlin.reflect.typeOf

fun NavGraphBuilder.authGraph(
    navController: NavHostController
) {
    navigation<AuthBaseRoute>(
        startDestination = LoginRoute
    ) {
        composable<LoginRoute> {
            LoginScreenRoot(
                onRegisterClick = {
                    navController.navigateToRegisterScreen {
                        popUpTo<LoginRoute> {
                            inclusive = true
                            saveState = true
                        }
                        restoreState = true
                    }
                }
            )
        }

        composable<RegisterRoute> {
            RegisterScreenRoot(
                onAlreadyHaveAnAccountClick = {
                    navController.navigateToLoginRoute {
                        popUpTo<RegisterRoute> {
                            inclusive = true
                            saveState = true
                        }
                        restoreState = true
                    }
                },
                onNavigateToPinScreen = {
                    navController.navigateToCreatePinScreen(screenData = it) {
                        restoreState = false
                    }
                }
            )
        }

        composable<CreatePinRoute>(
            typeMap = mapOf(typeOf<CreatePinData>() to SerializableNavType.create(serializer<CreatePinData>()))
        ) {
            CreatePinScreenRoot(
                onNavigateToConfirmScreen = {
                    navController.navigateToConfirmPinScreen(
                        screenData = it
                    ) {
                        popUpTo<CreatePinRoute> {
                            inclusive = true
                        }
                    }
                },
                onNavigateToRegisterScreen = {
                    navController.navigateToRegisterScreen()
                }
            )
        }

        composable<ConfirmPinRoute>(
            typeMap = mapOf(typeOf<CreatePinData>() to SerializableNavType.create(serializer<CreatePinData>()))
        ) {
            ConfirmPinScreenRoot(
                onNavigateToRegisterScreen = {
                    navController.navigateToRegisterScreen()
                },
                onNavigateToPreferencesScreen = {

                }
            )
        }
    }
}