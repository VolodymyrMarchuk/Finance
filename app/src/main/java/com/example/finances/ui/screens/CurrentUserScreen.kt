package com.example.finances.ui.screens

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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.finances.data.Users

@Composable
fun CurrentUserScreen(
    currentUser: Users?
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
                ) {
                    Column (
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(5.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = currentUser?.userOnline.toString(),
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Icon(
                            imageVector = Icons.Default.Face,
                            contentDescription = null
                        )
                    }
                }
                Card (
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(5.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = currentUser?.userName.toString(),
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Icon(
                            imageVector = Icons.Default.Create,
                            contentDescription = null
                        )
                    }
                }
            }
        }
    }
}


//@Preview(showBackground = true)
//@Composable
//fun CurrentUserScreenPreview() {
//    FinancesTheme {
//        CurrentUserScreen(Users())
//    }
//}