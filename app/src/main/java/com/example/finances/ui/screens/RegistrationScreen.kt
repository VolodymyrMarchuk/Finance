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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.finances.ui.theme.FinancesTheme


@Composable
fun RegistrationScreen(
    onRegister: (
        userLogin: String,
        userPassword: String,
        userName: String,
        userSurname: String,
        userPhone: String,
        userMail: String
            ) -> Unit
) {
    val login = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
    val name = remember { mutableStateOf("") }
    val surname = remember { mutableStateOf("") }
    val phone = remember { mutableStateOf("") }
    val mail = remember { mutableStateOf("") }

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
                    text = "Registration:",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
                OutlinedTextField(
                    value = name.value,
                    label = {
                        Text(text = "Name")
                    },
                    onValueChange = { name.value = it }
                )
                OutlinedTextField(
                    value = surname.value,
                    label = {
                        Text(text = "Surname")
                    },
                    onValueChange = { surname.value = it }
                )
                OutlinedTextField(
                    value = phone.value,
                    label = {
                        Text(text = "Mobile")
                    },
                    onValueChange = { phone.value = it }
                )
                OutlinedTextField(
                    value = mail.value,
                    label = {
                        Text(text = "Mail")
                    },
                    onValueChange = { mail.value = it }
                )
                OutlinedTextField(
                    value = login.value,
                    label = {
                        Text(text = "Login")
                    },
                    onValueChange = { login.value = it }
                )
                OutlinedTextField(
                    value = password.value,
                    label = {
                        Text(text = "Password")
                    },
                    onValueChange = { password.value = it },
                    visualTransformation = PasswordVisualTransformation()
                )
                OutlinedButton(
                    onClick = {onRegister(
                        login.value, password.value, name.value, surname.value, phone.value, mail.value
                    )},
                    modifier = Modifier.padding(top = 15.dp)
                ) {
                    Text(text = "Register")
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun RegistrationScreenPreview() {
    FinancesTheme {
        RegistrationScreen(onRegister = {userLogin, userPassword, userName, userSurname, userPhone, userMail -> })
    }
}