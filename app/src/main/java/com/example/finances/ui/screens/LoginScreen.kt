package com.example.finances.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.finances.ui.theme.FinancesTheme

@Composable
fun LoginScreen(
    userLogin: (
            login: String,
            password: String
            ) -> Unit
) {
    var login by remember { mutableStateOf("") }
    var isLoginNotEmpty by remember { mutableStateOf(false) }
    var password by remember { mutableStateOf("") }
    var isPasswordNotEmpty by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Card {
            Column (
                modifier = Modifier.padding(5.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "LogIn:",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                    )
                OutlinedTextField(
                    value = login,
                    label = {
                        Text(text = "Login")
                    },
                    onValueChange = {
                        login = it
                        isLoginNotEmpty = login.isNotEmpty()
                    },
                    isError = !isLoginNotEmpty,
                    singleLine = true
                )
                OutlinedTextField(
                    value = password,
                    label = {
                        Text(text = "Password")
                    },
                    onValueChange = {
                        password = it
                        isPasswordNotEmpty = password.isNotEmpty()
                        },
                    isError = !isPasswordNotEmpty,
                    singleLine = true,
                    visualTransformation = PasswordVisualTransformation()
                )
                if (isLoginNotEmpty and isPasswordNotEmpty) {
                    OutlinedButton(
                        onClick = { userLogin(login, password) },
                        modifier = Modifier.padding(top = 15.dp)
                    ) {
                        Text(text = "LogIn")
                    }
                } else {
                    OutlinedButton(
                        onClick = { },
                        modifier = Modifier.padding(top = 15.dp),
                        enabled = false
                    ) {
                        Text(text = "LogIn")
                    }
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    FinancesTheme {
        LoginScreen(userLogin = {login, password -> Unit})
    }
}