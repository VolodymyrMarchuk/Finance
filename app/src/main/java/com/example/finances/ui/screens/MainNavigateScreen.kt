package com.example.finances.ui.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.finances.data.Users
import com.example.finances.ui.FinanceViewModel
import com.example.finances.ui.theme.FinancesTheme
import kotlinx.coroutines.launch


enum class ScreenManager {
    FINANCE_START_SCREEN,
    FINANCE_LOGIN_SCREEN,
    FINANCE_REGISTER_SCREEN,
    FINANCE_CURRENTUSER_SCREEN,
    FINANCE_UPDATEUSER_SCREEN
}

@SuppressLint("RememberReturnType")
@Composable
fun FinanceApp(
    financeNavHostController: NavHostController = rememberNavController(),
    viewModel: FinanceViewModel = viewModel(factory = FinanceViewModel.factory),
    modifier: Modifier = Modifier
) {
    val currentUser by viewModel.userLogin().collectAsState(initial = null)



    val snackbarHostState = remember {
        SnackbarHostState()
    }
    val scope = rememberCoroutineScope()
    ObserveAsEvents(flow = SnackbarController.events, snackbarHostState) {event ->
        scope.launch {
            snackbarHostState.currentSnackbarData?.dismiss()

            val result = snackbarHostState.showSnackbar(
                message = event.message,
                actionLabel = event.action?.name,
                duration = SnackbarDuration.Short
            )

            if (result == SnackbarResult.ActionPerformed) {
               event.action?.action?.invoke()
            }
        }
    }
    Scaffold(
        snackbarHost = {
            SnackbarHost(
                hostState = snackbarHostState
            )
        },
        modifier = Modifier.fillMaxSize(),
        topBar = { FinanceAppBar(
            currentUser,
            onLogIn = { financeNavHostController.navigate(ScreenManager.FINANCE_LOGIN_SCREEN.name) },
            onRegister = { financeNavHostController.navigate(ScreenManager.FINANCE_REGISTER_SCREEN.name) },
            onLogOut = { userId->
                viewModel.userOffline(userId)
                financeNavHostController.navigate(ScreenManager.FINANCE_START_SCREEN.name)
            },
            onUpdate = { financeNavHostController.navigate(ScreenManager.FINANCE_UPDATEUSER_SCREEN.name) }
            ) }
    ) { innerPadding ->
        NavHost(
            navController = financeNavHostController,
            modifier = Modifier.padding(innerPadding),
            startDestination = ScreenManager.FINANCE_START_SCREEN.name
        ) {
            composable(route = ScreenManager.FINANCE_START_SCREEN.name) {
                if (currentUser != null) {
                    CurrentUserScreen(currentUser, logOut = {userId->
                        if(userId != null) {
                            viewModel.userOffline(userId)
                            financeNavHostController.navigate(route = ScreenManager.FINANCE_START_SCREEN.name)
                        }
                    })
                } else {
                StartScreen(
                    onLoginClick = { financeNavHostController.navigate(ScreenManager.FINANCE_LOGIN_SCREEN.name) },
                    onRegisterClick = { financeNavHostController.navigate(ScreenManager.FINANCE_REGISTER_SCREEN.name) }
                )
                    }
            }
            composable(route = ScreenManager.FINANCE_LOGIN_SCREEN.name) {
                LoginScreen(userLogin = {login, password ->
                    viewModel.verifyUser(login, password)
                    financeNavHostController.navigate(ScreenManager.FINANCE_CURRENTUSER_SCREEN.name)
                })
            }
            composable(route = ScreenManager.FINANCE_REGISTER_SCREEN.name) {
                RegistrationScreen(
                    onRegister = {login, password, name, surname, phone, mail ->
                        viewModel.userRegistration(
                            userLogin = login,
                            userPassword = password,
                            userName = name,
                            userSurname = surname,
                            userPhone = phone,
                            userMail = mail
                        )

                    }
                )
            }
            composable(route = ScreenManager.FINANCE_CURRENTUSER_SCREEN.name) {
                CurrentUserScreen(currentUser, logOut = {userId->
                    if(userId != null) {
                        viewModel.userOffline(userId)
                        financeNavHostController.navigate(route = ScreenManager.FINANCE_START_SCREEN.name)
                    }
                })
            }
            composable(route = ScreenManager.FINANCE_UPDATEUSER_SCREEN.name) {
                UserUpdateScreen(currentUser, onUpdate = {userId, userLogin, userPassword, userName, userSurname, userPhone, userMail ->
                viewModel.userUpdate(
                    id = userId,
                    name = userName,
                    surname = userSurname,
                    phone = userPhone,
                    mail = userMail,
                    password = userPassword
                )
                    financeNavHostController.navigate(ScreenManager.FINANCE_CURRENTUSER_SCREEN.name)
                })
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FinanceAppBar(
    user: Users?,
    onLogIn: () -> Unit,
    onLogOut: (userId: Int) -> Unit,
    onRegister: () -> Unit,
    onUpdate: () -> Unit
    ) {
    var expanded by remember { mutableStateOf(false) }
    CenterAlignedTopAppBar(
        title = {
            Text(text = "Finance")
        },
        actions = {
            Row {
                IconButton(
                    onClick = {}
                ) {
                    Icon(
                        imageVector = Icons.Filled.List,
                        contentDescription = "List or Grid"
                    )
                }
                if (user != null) {
                    IconButton(
                        onClick = { expanded = !expanded }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.AccountCircle, tint = Color.Green,
                            contentDescription = "User"
                        )
                        DropdownMenu(
                            expanded = expanded,
                            onDismissRequest = {expanded = false}
                        ) {
                            DropdownMenuItem(
                                text = { Text(text = "Profile") },
                                onClick = {
                                    expanded = false
                                    onUpdate()
                                }
                            )
                            DropdownMenuItem(
                                text = { Text(text = "Settings") },
                                onClick = {
                                    expanded = false
                                }
                            )
                            DropdownMenuItem(
                                text = { Text(text = "Help") },
                                onClick = {
                                    expanded = false
                                }
                            )
                            DropdownMenuItem(
                                text = { Text(text = "LogOut") },
                                onClick = {
                                    expanded = false
                                    onLogOut(user.userId)

                                }
                            )
                        }
                    }
                } else {
                    IconButton(
                        onClick = { expanded = !expanded }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.AccountCircle, tint = Color.Red,
                            contentDescription = "User"
                        )
                        DropdownMenu(
                            expanded = expanded,
                            onDismissRequest = {expanded = false}
                        ) {
                            DropdownMenuItem(
                                text = { Text(text = "LogIn") },
                                onClick = {
                                    expanded = false
                                    onLogIn()
                                }
                            )
                            DropdownMenuItem(
                                text = { Text(text = "Registration") },
                                onClick = {
                                    expanded = false
                                    onRegister()
                                }
                            )
                        }
                    }
                }
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun FinanceAppBarPreview() {
    FinancesTheme {
        FinanceAppBar(
            user = null,
            onLogIn = {},
            onLogOut = {},
            onRegister = {},
            onUpdate = {})
    }
}