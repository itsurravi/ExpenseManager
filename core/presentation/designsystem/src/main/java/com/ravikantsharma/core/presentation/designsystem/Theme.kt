package com.ravikantsharma.core.presentation.designsystem

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf

val primaryFixed = ExManagerPrimaryFixed
val onPrimaryFixed = ExManagerOnPrimaryFixed
val secondaryFixed = ExManagerSecondaryFixed
val secondaryFixedDim = ExManagerSecondaryFixedDim
val success = ExManagerSuccess

private val ColorScheme = darkColorScheme(
    primary = ExManagerPurple,
    onPrimary = ExManagerWhite,
    secondary = ExManagerSecondary,
    onSurface = ExManagerBlack,
    onSurfaceVariant = ExManagerDarkGrey,
    error = ExManagerRed,
    background = ExManagerPaleLavender,
    onBackground = ExManagerLightGrey,
    primaryContainer = ExManagerPrimaryContainer,
    onPrimaryContainer = ExManagerOnPrimaryContainer,
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