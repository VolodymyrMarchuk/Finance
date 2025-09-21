package com.example.finances.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Face
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.finances.ui.theme.FinancesTheme

@Composable
fun StartScreen(
    onLoginClick: () -> Unit,
    onRegisterClick: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.TopStart
    ) {
            Column (
                modifier = Modifier.fillMaxWidth().padding(5.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row (
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Card (
                        modifier = Modifier
                            .fillMaxWidth(0.4f)
                            .padding(10.dp)
                            .clickable(
                                enabled = true,
                                onClick = onLoginClick
                            )
                    ) {
                        Column (
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(5.dp),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "LogIn",
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Icon(
                                imageVector = Icons.Default.Face,
                                contentDescription = "Login"
                            )
                        }
                    }
                    Card (
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp)
                            .clickable(
                                enabled = true,
                                onClick = onRegisterClick
                            )
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(5.dp),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "Register",
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Icon(
                                imageVector = Icons.Default.Create,
                                contentDescription = "Register"
                            )
                        }
                    }
                }
            }
        }
    }


@Preview(showBackground = true)
@Composable
fun StartScreenPreview() {
    FinancesTheme {
        StartScreen(onLoginClick = {}, onRegisterClick = {})
    }
}