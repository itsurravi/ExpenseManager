package com.ravikantsharma.settings.presentation.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.ravikantsharma.settings.presentation.home.SettingsHomeScreenRoot

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

                },
                onNavigateToSettings = {

                },
                onLogout = onLogout
            )
        }
    }
}