package com.ravikantsharma.ui.navigation

import androidx.navigation.NavController
import androidx.navigation.NavOptionsBuilder
import kotlinx.serialization.Serializable

@Serializable
data object DashboardBaseRoute: AppRoute

@Serializable
data object DashboardScreenRoute: AppRoute

@Serializable
data object AllTransactionsScreenRoute: AppRoute

fun NavController.navigateToDashboardScreen(
    navOptions: NavOptionsBuilder.() -> Unit = {}
) = navigate(DashboardScreenRoute, navOptions)

fun NavController.navigateToAllTransactionsScreenRoute(
    navOptions: NavOptionsBuilder.() -> Unit = {}
) = navigate(AllTransactionsScreenRoute, navOptions)