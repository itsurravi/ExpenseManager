package com.ravikantsharma.expensemanager.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.ravikantsharma.auth.presentation.navigation.authGraph
import com.ravikantsharma.dashboard.presentation.navigation.dashboardNavGraph
import com.ravikantsharma.session_management.presentation.navigation.sessionNavGraph
import com.ravikantsharma.settings.presentation.navigation.settingsNavGraph
import com.ravikantsharma.ui.AppNavRoute
import com.ravikantsharma.ui.LocalAuthActionHandler
import com.ravikantsharma.ui.LocalAuthNavigationHandler
import com.ravikantsharma.ui.NavigationRequestHandler
import com.ravikantsharma.ui.navigation.AuthBaseRoute
import com.ravikantsharma.ui.navigation.SettingsHomeScreenRoute
import com.ravikantsharma.ui.navigation.navigateToDashboardScreen
import com.ravikantsharma.ui.navigation.navigateToSettingsHomeScreen

@Composable
fun NavigationRoot(
    navController: NavHostController,
    navigationRequestHandler: NavigationRequestHandler,
    modifier: Modifier = Modifier,
    onSessionVerified: () -> Unit = {},
    onLogout: () -> Unit = {},
) {
    CompositionLocalProvider(
        LocalAuthActionHandler provides { onVerified: () -> Unit ->
            navigationRequestHandler.runWithAuthCheck(onVerified)
        },
        LocalAuthNavigationHandler provides { appNavRoute: AppNavRoute ->
            navigationRequestHandler.navigateWithAuthCheck(appNavRoute)
        }
    ) {
        NavHost(
            navController = navController,
            startDestination = AuthBaseRoute,
            modifier = modifier,
        ) {
            authGraph(
                navController = navController,
                onNavigateToDashboardScreen = {
                    navController.navigateToDashboardScreen {
                        popUpTo<AuthBaseRoute> { inclusive = true }
                    }
                }
            )
            dashboardNavGraph(
                navController = navController,
                onNavigateToSettings = {
                    navigationRequestHandler.navigateWithAuthCheck(
                        AppNavRoute(
                            pendingRoute = SettingsHomeScreenRoute
                        )
                    )
                }
            )
            sessionNavGraph(
                navController = navController,
                onVerificationSuccess = {
                    onSessionVerified()
                    navController.popBackStack()
                },
                onLogout = {
                    onLogout()
                }
            )
            settingsNavGraph(
                navController = navController,
                onLogout = {
                    onLogout()
                }
            )
        }
    }
}