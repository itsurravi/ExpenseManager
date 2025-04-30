package com.ravikantsharma.dashboard.presentation.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.ravikantsharma.dashboard.presentation.dashboard.DashboardScreen

fun NavGraphBuilder.dashboardNavGraph(
    navController: NavController
) {
    navigation<DashboardBaseRoute>(
        startDestination = DashboardScreenRoute
    ) {
        composable<DashboardScreenRoute> {
            DashboardScreen()
        }
    }
}