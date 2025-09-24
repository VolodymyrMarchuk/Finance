package com.example.finances.ui.screens

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.CenterAlignedTopAppBar
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.finances.ui.FinanceViewModel
import kotlinx.coroutines.launch


enum class ScreenManager {
    FINANCE_START_SCREEN,
    FINANCE_LOGIN_SCREEN,
    FINANCE_REGISTER_SCREEN
}

@SuppressLint("RememberReturnType")
@Composable
fun FinanceApp(
    financeNavHostController: NavHostController = rememberNavController(),
    viewModel: FinanceViewModel = viewModel(factory = FinanceViewModel.factory),
    modifier: Modifier = Modifier
) {
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
        topBar = { FinanceAppBar() }
    ) { innerPadding ->
        NavHost(
            navController = financeNavHostController,
            modifier = Modifier.padding(innerPadding),
            startDestination = ScreenManager.FINANCE_START_SCREEN.name
        ) {
            composable(route = ScreenManager.FINANCE_START_SCREEN.name) {
                StartScreen(
                    onLoginClick = { financeNavHostController.navigate(ScreenManager.FINANCE_LOGIN_SCREEN.name) },
                    onRegisterClick = { financeNavHostController.navigate(ScreenManager.FINANCE_REGISTER_SCREEN.name) }
                )
            }
            composable(route = ScreenManager.FINANCE_LOGIN_SCREEN.name) {
                LoginScreen()
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
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FinanceAppBar() {
    CenterAlignedTopAppBar(
        title = {
            Text(text = "Finance")
        },
        actions = {
            IconButton(
                onClick = {}
            ) {
                Icon(
                    imageVector = Icons.Filled.List,
                    contentDescription = "List or Grid"
                )
            }
        }
    )
}