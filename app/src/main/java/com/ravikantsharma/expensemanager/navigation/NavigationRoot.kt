package com.ravikantsharma.expensemanager.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.ravikantsharma.auth.presentation.navigation.AuthBaseRoute
import com.ravikantsharma.auth.presentation.navigation.authGraph

@Composable
fun NavigationRoot(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = AuthBaseRoute,
        modifier = modifier,
    ) {
        authGraph(navController = navController)
    }
}