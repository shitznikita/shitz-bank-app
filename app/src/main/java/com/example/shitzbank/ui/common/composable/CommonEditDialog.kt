package com.example.shitzbank.ui.common.composable

import androidx.compose.runtime.Composable
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.window.DialogProperties
import com.example.shitzbank.R

@Composable
fun CommonEditDialog(
    title: String,
    confirmButtonEnabled: Boolean = true,
    onDismiss: () -> Unit,
    onSave: () -> Unit,
    content: @Composable () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            CommonText(
                text = title,
                color = MaterialTheme.colorScheme.onPrimary,
                style = MaterialTheme.typography.bodyLarge
            )
        },
        text = content,
        confirmButton = {
            Button (
                onClick = onSave,
                enabled = confirmButtonEnabled
            ) {
                CommonText(
                    text = stringResource(R.string.save),
                    color = MaterialTheme.colorScheme.onPrimary,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                CommonText(
                    text = stringResource(R.string.cancel),
                    color = MaterialTheme.colorScheme.onPrimary,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        },
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = true
        )
    )
}