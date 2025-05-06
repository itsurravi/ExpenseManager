package com.ravikantsharma.settings.presentation.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.ravikantsharma.settings.presentation.home.SettingsHomeScreenRoot
import com.ravikantsharma.settings.presentation.preference.SettingsPreferenceScreenRoot


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
    }
}