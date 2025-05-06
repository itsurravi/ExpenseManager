package com.ravikantsharma.settings.presentation.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.ravikantsharma.settings.presentation.home.SettingsHomeScreenRoot
import com.ravikantsharma.settings.presentation.preference.SettingsPreferenceScreenRoot
import com.ravikantsharma.settings.presentation.security.SettingsSecurityScreenRoot


fun NavGraphBuilder.settingsNavGraph(
    navController: NavHostController,
    onLogout: () -> Unit
) {
    navigation<SettingsBaseRoute>(
        startDestination = SettingsHomeScreenRoute
    ) {
        composable<SettingsHomeScreenRoute> {
            SettingsHomeScreenRoot(
                onNavigateToPreference = {
                    navController.navigateToSettingsPreferenceScreen()
                },
                onNavigateToSettings = {
                    navController.navigateToSettingsSecurityScreen()
                },
                onLogout = onLogout,
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
        composable<SettingsPreferenceScreenRoute> {
            SettingsPreferenceScreenRoot(
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
        composable<SettingsSecurityScreenRoute> {
            SettingsSecurityScreenRoot(
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}