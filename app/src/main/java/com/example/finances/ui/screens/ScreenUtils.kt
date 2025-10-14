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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddSource(
    onDismissRequest: () -> Unit,
    onConfirmation: (String) -> Unit
) {
    var newSource by remember { mutableStateOf("") }

    AlertDialog(
        icon = {
            Icon(imageVector = Icons.Default.Add, contentDescription = "Add")
        },
        title = {
            Text(text = "New Source")
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
                    Text("Create")
                }
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismissRequest
            ) {
                Text("Cancel")
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
                Text(text = "Cancel")
            }
        }
    ) {
        DatePicker(state = datePickerState)
    }
}


