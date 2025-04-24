package com.ravikantsharma.designsystem.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ravikantsharma.designsystem.BackDelete
import com.ravikantsharma.designsystem.ExManagerOnPrimaryFixed
import com.ravikantsharma.designsystem.ExpenseManagerTheme
import com.ravikantsharma.designsystem.FingerPrint

@Composable
fun ExManagerPinPad(
    modifier: Modifier = Modifier,
    hasBiometricButton: Boolean? = false,
    onBiometricButtonClicked: (() -> Unit)? = null,
    onNumberPressedClicked: (Int) -> Unit,
    onDeletePressedClicked: () -> Unit,
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(4.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        items(9) { index ->
            PinPadButton(
                text = (index + 1).toString(),
                onClick = {
                    onNumberPressedClicked(index + 1)
                }
            )
        }

        items(3) { index ->
            when (index) {
                0 -> {
                    if (hasBiometricButton == true) {
                        PinPadButton(
                            icon = FingerPrint,
                            onClick = {
                                onBiometricButtonClicked?.invoke()
                            },
                            alpha = 0.3f
                        )
                    } else {
                        Spacer(modifier = Modifier.size(108.dp))
                    }
                }

                1 -> {
                    PinPadButton(
                        text = "0",
                        onClick = { onNumberPressedClicked(0) }
                    )
                }

                2 -> {
                    PinPadButton(
                        icon = BackDelete,
                        onClick = onDeletePressedClicked,
                        alpha = 0.3f
                    )
                }
            }
        }
    }
}

@Composable
private fun PinPadButton(
    modifier: Modifier = Modifier,
    text: String? = null,
    icon: ImageVector? = null,
    onClick: (() -> Unit)? = null,
    alpha: Float = 1f
) {
    Box(
        modifier = modifier
            .size(108.dp)
            .clip(RoundedCornerShape(32.dp))
            .background(MaterialTheme.colorScheme.primaryContainer.copy(alpha = alpha))
            .clickable(enabled = onClick != null) { onClick?.invoke() },
        contentAlignment = Alignment.Center
    ) {
        text?.let {
            Text(
                text = it,
                style = MaterialTheme.typography.headlineLarge.copy(
                    color = ExManagerOnPrimaryFixed
                )
            )
        }

        icon?.let {
            Image(
                imageVector = it,
                contentDescription = null
            )
        }
    }
}

@Preview
@Composable
private fun PreviewExManagerPinPad() {
    ExpenseManagerTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            ExManagerPinPad(
                modifier = Modifier.padding(16.dp),
                hasBiometricButton = true,
                onNumberPressedClicked = {

                },
                onDeletePressedClicked = {

                }
            )
        }
    }
}