package com.example.shitzbank.ui.common.composable

import androidx.compose.foundation.background
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.shitzbank.R

@Composable
fun CustomTimePickerDialog(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    content: @Composable () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        modifier = Modifier.background(MaterialTheme.colorScheme.secondary),
        properties = androidx.compose.ui.window.DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = true
        ),
        dismissButton = {
            TextButton(onClick = onDismiss) {
                CommonText(
                    text = stringResource(R.string.cancel),
                    color = MaterialTheme.colorScheme.onPrimary,
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
        },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                CommonText(
                    text = stringResource(R.string.ok),
                    color = MaterialTheme.colorScheme.onPrimary,
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
        },
        text = { content() }
    )
}