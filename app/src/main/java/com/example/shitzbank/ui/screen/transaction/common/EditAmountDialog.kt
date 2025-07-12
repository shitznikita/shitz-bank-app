package com.example.shitzbank.ui.screen.transaction.common

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import com.example.shitzbank.ui.common.composable.CommonEditDialog
import com.example.shitzbank.R

@Composable
fun EditAmountDialog(
    currentAmount: String,
    onAmountChange: (String) -> Unit,
    onDismiss: () -> Unit,
    onSave: () -> Unit
) {
    val isAmountValid = currentAmount.isNotBlank()
            && currentAmount.replace(',', '.').toDoubleOrNull() != null

    CommonEditDialog(
        title = stringResource(R.string.amount),
        confirmButtonEnabled = isAmountValid,
        onDismiss = onDismiss,
        onSave = onSave,
        content = {
            OutlinedTextField(
                value = currentAmount,
                onValueChange = { newValue ->
                    val filteredValue = newValue.filter { it.isDigit() || it == '.' || it == ',' }
                    if (filteredValue.count { it == '.' } <= 1 && filteredValue.count { it == ',' } <= 1) {
                        onAmountChange(filteredValue)
                    }
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true
            )
        }
    )
}