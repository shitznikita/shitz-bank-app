package com.example.shitzbank.ui.screen.account.common

import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.shitzbank.R
import com.example.shitzbank.ui.common.composable.CommonEditDialog

@Composable
fun EditAccountNameDialog(
    currentName: String,
    onNameChange: (String) -> Unit,
    onDismiss: () -> Unit,
    onSave: () -> Unit
) {
    CommonEditDialog(
        title = stringResource(R.string.balance_name),
        confirmButtonEnabled = currentName.isNotBlank(),
        onDismiss = onDismiss,
        onSave = onSave,
        content = {
            OutlinedTextField(
                value = currentName,
                onValueChange = onNameChange,
                singleLine = true
            )
        }
    )
}