package com.ravikantsharma.auth.presentation.user_preference

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.ravikantsharma.designsystem.ExpenseManagerTheme

@Composable
fun PreferencesScreenRoot(
    modifier: Modifier = Modifier
) {
    PreferencesScreen()
}

@Composable
fun PreferencesScreen(
    modifier: Modifier = Modifier
) {

}

@Preview
@Composable
fun PreviewPreferencesScreen() {
    ExpenseManagerTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            PreferencesScreen()
        }
    }
}