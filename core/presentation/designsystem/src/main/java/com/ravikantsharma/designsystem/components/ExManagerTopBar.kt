package com.ravikantsharma.designsystem.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.ravikantsharma.designsystem.BackArrow

@Composable
fun ExManagerTopBar(
    modifier: Modifier = Modifier,
    title: String? = null,
    startIcon: ImageVector? = BackArrow,
    onStartIconClick: (() -> Unit)? = null,
    endIcon: ImageVector? = null,
    onEndIconClick: (() -> Unit)? = null
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .defaultMinSize(minHeight = 64.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        startIcon?.let {
            Icon(
                modifier = Modifier
                    .padding(16.dp)
                    .clickable {
                        onStartIconClick?.invoke()
                    },
                imageVector = it,
                contentDescription = ""
            )
        }

        title?.let {
            Text(
                modifier = Modifier.padding(
                    vertical = 16.dp
                ),
                text = it
            )
        }

        Spacer(modifier = Modifier.weight(1f))
        endIcon?.let {
            Icon(
                imageVector = it,
                contentDescription = "",
                modifier = Modifier
                    .padding(16.dp)
                    .clickable {
                        onEndIconClick?.invoke()
                    }
            )
        }
    }
}