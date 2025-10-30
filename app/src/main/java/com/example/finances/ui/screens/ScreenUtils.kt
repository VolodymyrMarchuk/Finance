package com.example.finances.ui.screens

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import com.example.finances.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddSource(
    onDismissRequest: () -> Unit,
    onConfirmation: (String) -> Unit
) {
    var newSource by remember { mutableStateOf("") }

    AlertDialog(
        icon = {
            Icon(imageVector = Icons.Default.Add, contentDescription = stringResource(R.string.add))
        },
        title = {
            Text(text = stringResource(R.string.new_source))
        },
        text = {
            OutlinedTextField(
                value = newSource,
                onValueChange = {
                    newSource = it

                }
            )
        },
        onDismissRequest = onDismissRequest,
        confirmButton = {
            if(newSource.isNotEmpty()) {
                TextButton(
                    onClick = {
                        onConfirmation(newSource)
                    }
                ) {
                    Text(stringResource(R.string.create))
                }
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismissRequest
            ) {
                Text(stringResource(R.string.cancel))
            }
        }
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChooseDate(
    onDismiss: () -> Unit,
    onDateSelected: (Long?) -> Unit
) {
    val datePickerState = rememberDatePickerState()
    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = {
                onDateSelected(datePickerState.selectedDateMillis)
                onDismiss
            }) {
                Text(text = "Ok")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(text = stringResource(R.string.cancel))
            }
        }
    ) {
        DatePicker(state = datePickerState)
    }
}


