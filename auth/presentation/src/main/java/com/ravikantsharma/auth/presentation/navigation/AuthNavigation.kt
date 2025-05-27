package com.ravikantsharma.auth.presentation.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.ravikantsharma.auth.presentation.create_pin.ConfirmPinScreenRoot
import com.ravikantsharma.auth.presentation.create_pin.CreatePinScreenRoot
import com.ravikantsharma.auth.presentation.login.LoginScreenRoot
import com.ravikantsharma.ui.navigation.CreatePinScreenData
import com.ravikantsharma.ui.navigation.PreferencesScreenData
import com.ravikantsharma.auth.presentation.register.RegisterScreenRoot
import com.ravikantsharma.auth.presentation.user_preference.OnboardingPreferencesScreenRoot
import com.ravikantsharma.ui.SerializableNavType
import com.ravikantsharma.ui.navigation.AuthBaseRoute
import com.ravikantsharma.ui.navigation.ConfirmPinRoute
import com.ravikantsharma.ui.navigation.CreatePinRoute
import com.ravikantsharma.ui.navigation.LoginRoute
import com.ravikantsharma.ui.navigation.PreferencesRoute
import com.ravikantsharma.ui.navigation.RegisterRoute
import com.ravikantsharma.ui.navigation.navigateToConfirmPinScreen
import com.ravikantsharma.ui.navigation.navigateToCreatePinScreen
import com.ravikantsharma.ui.navigation.navigateToLoginRoute
import com.ravikantsharma.ui.navigation.navigateToPreferencesScreen
import com.ravikantsharma.ui.navigation.navigateToRegisterScreen
import kotlinx.serialization.serializer
import kotlin.reflect.typeOf

fun NavGraphBuilder.authGraph(
    navController: NavHostController,
    onNavigateToDashboardScreen: () -> Unit
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
                },
                onNavigateToDashboard = onNavigateToDashboardScreen
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
            typeMap = mapOf(typeOf<CreatePinScreenData>() to SerializableNavType.create(serializer<CreatePinScreenData>()))
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
                onBackClick = {
                    navController.navigateToRegisterScreen {
                        popUpTo<AuthBaseRoute> {
                            inclusive = true
                        }
                    }
                }
            )
        }

        composable<ConfirmPinRoute>(
            typeMap = mapOf(typeOf<CreatePinScreenData>() to SerializableNavType.create(serializer<CreatePinScreenData>()))
        ) {
            ConfirmPinScreenRoot(
                onBackClick = {
                    navController.popBackStack()
                },
                onNavigateToPreferencesScreen = {
                    navController.navigateToPreferencesScreen(it) {
                        popUpTo<ConfirmPinRoute> {
                            inclusive = true
                        }
                    }
                }
            )
        }

        composable<PreferencesRoute>(
            typeMap = mapOf(typeOf<PreferencesScreenData>() to SerializableNavType.create(serializer<PreferencesScreenData>()))
        ) {
            OnboardingPreferencesScreenRoot(
                onNavigateBack = {
                    navController.popBackStack()
                },
                onNavigateToDashboardScreen = onNavigateToDashboardScreen
            )
        }
    }
}