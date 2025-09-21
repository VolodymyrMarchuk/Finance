package com.example.finances

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.example.finances.ui.screens.FinanceApp
import com.example.finances.ui.theme.FinancesTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FinancesTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    FinanceApp(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

