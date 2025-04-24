package com.ravikantsharma.designsystem

import android.app.Activity
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val ColorScheme = darkColorScheme(
    primary = ExManagerPurple,
    onPrimary = ExManagerWhite,
    onSurface = ExManagerBlack,
    onSurfaceVariant = ExManagerDarkGrey,
    error = ExManagerRed,
    background = ExManagerPaleLavender,
    onBackground = ExManagerLightGrey,
    primaryContainer = ExManagerPrimaryFixed,
    onPrimaryContainer = ExManagerOnPrimaryFixed,
    surfaceContainerLowest = ExManagerSurfaceContainerLowest,
    surfaceContainerLow = ExManagerSurfaceContainerLow,
    inversePrimary = ExManagerInversePrimary,
    secondaryContainer = ExManagerSecondaryContainer,
    onSecondaryContainer = ExManagerOnSecondaryContainer,
    tertiaryContainer = ExManagerTertiaryContainer
)

data class StatusBarAppearance(
    val isDarkStatusBarIcons: Boolean = true
)

val LocalStatusBarAppearance = compositionLocalOf { StatusBarAppearance() }

@Composable
fun ExpenseManagerTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = ColorScheme,
        typography = Typography,
        content = content
    )
}