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
    var login by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var surname by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var mail by remember { mutableStateOf("") }

    //Verify if email is correct
    var isValidMail by remember { mutableStateOf(false) }
    val emailRegex = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")

    //Verify length of Login (min 6 symbols) and Password (min 6 symbols)
    var isValidLengthLogin by remember { mutableStateOf(false) }
    var isValidLengthPassword by remember { mutableStateOf(false) }
    val loginRegex = Regex("^.{6,}$")
    val passwordRegex = Regex("^.{6,}$")

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
                    value = name,
                    label = {
                        Text(text = "Name")
                    },
                    onValueChange = { name = it }
                )
                OutlinedTextField(
                    value = surname,
                    label = {
                        Text(text = "Surname")
                    },
                    onValueChange = { surname = it }
                )
                OutlinedTextField(
                    value = phone,
                    label = {
                        Text(text = "Mobile")
                    },
                    onValueChange = { phone = it }
                )
                OutlinedTextField(
                    value = mail,
                    label = {
                        Text(text = "Mail")
                    },
                    onValueChange = {
                        mail = it
                        isValidMail = emailRegex.matches(it)
                    },
                    isError = !isValidMail,
                    singleLine = true
                )

                OutlinedTextField(
                    value = login,
                    label = {
                        Text(text = "Login")
                    },
                    onValueChange = {
                        login = it
                        isValidLengthLogin = loginRegex.matches(it)
                    },
                    isError = !isValidLengthLogin,
                    singleLine = true
                )
                OutlinedTextField(
                    value = password,
                    label = {
                        Text(text = "Password")
                    },
                    onValueChange = {
                        password = it
                        isValidLengthPassword = passwordRegex.matches(it)
                    },
                    isError = !isValidLengthPassword,
                    singleLine = true,
                    visualTransformation = PasswordVisualTransformation()
                )
                if (isValidMail and isValidLengthLogin and isValidLengthPassword ) {
                    OutlinedButton(
                        onClick = {
                            onRegister(
                                login, password, name, surname, phone, mail
                            )
                            login = ""
                            mail = ""
                            password = ""
                            name = ""
                            surname = ""
                            phone = ""
                            isValidLengthLogin = false
                            isValidLengthPassword = false
                            isValidMail = false
                        },
                        modifier = Modifier.padding(top = 15.dp),
                        enabled = true
                    ) {
                        Text(text = "Register")
                    }
                } else {
                    OutlinedButton(
                        onClick = { },
                        modifier = Modifier.padding(top = 15.dp),
                        enabled = false
                    ) {
                        Text(text = "Register")
                    }
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