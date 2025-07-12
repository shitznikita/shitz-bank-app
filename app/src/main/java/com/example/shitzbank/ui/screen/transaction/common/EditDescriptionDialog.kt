package com.example.shitzbank.ui.screen.transaction.common

import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.shitzbank.ui.common.composable.CommonEditDialog
import com.example.shitzbank.R

@Composable
fun EditDescriptionDialog(
    currentComment: String,
    onCommentChange: (String) -> Unit,
    onDismiss: () -> Unit,
    onSave: () -> Unit
) {
    CommonEditDialog(
        title = stringResource(R.string.description),
        confirmButtonEnabled = true,
        onDismiss = onDismiss,
        onSave = onSave
    ) {
        OutlinedTextField(
            value = currentComment,
            onValueChange = onCommentChange,
            singleLine = false
        )
    }
}