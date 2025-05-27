package com.ravikantsharma.expensemanager

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import com.ravikantsharma.core.presentation.designsystem.ExpenseManagerTheme
import com.ravikantsharma.expensemanager.navigation.NavigationRoot
import com.ravikantsharma.ui.navigateToRoute
import com.ravikantsharma.ui.navigation.AuthBaseRoute
import com.ravikantsharma.ui.navigation.SessionBaseRoute
import com.ravikantsharma.ui.navigation.navigateToLoginRoute
import com.ravikantsharma.ui.navigation.navigateToPinPromptScreen
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : ComponentActivity() {

    private val mainViewModel by viewModel<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val splashScreen = installSplashScreen()
        splashScreen.setKeepOnScreenCondition {
            mainViewModel.uiState.value.isCheckingAuth
        }

        setContent {
            ExpenseManagerTheme {
                val uiState by mainViewModel.uiState.collectAsStateWithLifecycle()
                if (!uiState.isCheckingAuth) {
                    val navController = rememberNavController()
                    LaunchedEffect(uiState.showPinPrompt) {
                        navController.navigateToPinPromptScreen {
                            popUpTo<SessionBaseRoute> {
                                inclusive = true
                            }
                            launchSingleTop = true
                        }
                    }

                    uiState.pendingRoute?.let { route ->
                        if (!uiState.isSessionExpired && uiState.isUserLoggedIn) {
                            navController.navigateToRoute(route)
                            mainViewModel.mainViewModelClearPendingRoute()
                        }
                    }

                    NavigationRoot(
                        navController = navController,
                        navigationRequestHandler = mainViewModel,
                        onSessionVerified = {
                            mainViewModel.startSession()
                            uiState.pendingActionAfterAuth?.invoke()
                            mainViewModel.onPinVerified()
                            mainViewModel.clearPendingActionAfterAuth()
                        },
                        onLogout = {
                            navController.navigateToLoginRoute {
                                popUpTo(navController.graph.id) {
                                    inclusive = true
                                }
                            }
                        },
                        authNavigationDestination = uiState.authNavigationDestination
                    )
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        mainViewModel.onAppResumed()
    }
}