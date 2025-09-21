package com.example.finances.ui.screens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController


enum class ScreenManager {
    FINANCE_START_SCREEN,
    FINANCE_LOGIN_SCREEN,
    FINANCE_REGISTER_SCREEN
}

@Composable
fun FinanceApp(
    financeNavHostController: NavHostController = rememberNavController(),
    modifier: Modifier = Modifier
) {
    Scaffold(
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
                RegistrationScreen()
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