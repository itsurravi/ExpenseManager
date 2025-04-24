package com.ravikantsharma.auth.presentation.user_preference

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.ravikantsharma.designsystem.ExpenseManagerTheme

@Composable
fun OnboardingPreferencesScreenRoot(
    modifier: Modifier = Modifier
) {
    OnboardingPreferencesScreen()
}

@Composable
fun OnboardingPreferencesScreen(
    modifier: Modifier = Modifier
) {

}

@Preview
@Composable
fun PreviewOnboardingPreferencesScreen() {
    ExpenseManagerTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            OnboardingPreferencesScreen()
        }
    }
}