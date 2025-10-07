package com.example.finances.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.outlined.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.finances.data.Users
import com.example.finances.ui.theme.FinancesTheme


@Composable
fun CurrentUserScreen(
    currentUser: Users?,
    logOut: (Int?) -> Unit,
    tryAgain: () -> Unit
) {
//    val context = LocalContext.current
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.TopStart
    ) {
        if(currentUser != null) {
            Column(
                modifier = Modifier.fillMaxWidth().padding(5.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Card(
                    modifier = Modifier
                        .padding(10.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(5.dp),
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = currentUser?.userName.toString() + "  " + currentUser?.userSurname.toString(),
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center
                        )
                        Text(
                            fontSize = 18.sp,
                            text = currentUser?.userLogin.toString(),
                            modifier = Modifier.padding(start = 10.dp)
                        )
                        Text(
                            fontSize = 18.sp,
                            text = currentUser?.userPhone.toString(),
                            modifier = Modifier.padding(start = 10.dp)
                        )
                        Text(
                            fontSize = 18.sp,
                            text = currentUser?.userMail.toString(),
                            modifier = Modifier.padding(start = 10.dp)
                        )
                    }
                }
                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth(0.4f)
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
                                text = "Costs",
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Icon(
                                imageVector = Icons.Default.Warning,
                                contentDescription = null
                            )
                        }
                    }
                    Card(
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
                                text = "Income",
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = null
                            )
                        }
                    }
                }
                if (currentUser?.userId != null) {
                    OutlinedButton(
                        onClick = { logOut(currentUser.userId) },
                        modifier = Modifier.padding(top = 15.dp)
                    ) {
                        Text(text = "LogOut")
                    }
                }
                SimpleTabs()
//            StaticCalendar()
//            Button(onClick = {
//                Toast.makeText(context, "Test message", Toast.LENGTH_SHORT).show()
//            }) {
//                Text(text = "click me!")
//            }
//
//
//
//            Text(
//                text = LocalDate.now().month.toString()
//
//            )
            }
        } else {
            Column(
                modifier = Modifier.fillMaxSize().padding(5.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(10.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(5.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Warning,
                            modifier = Modifier
                                .padding(bottom = 100.dp)
                                .size(100.dp),
                            tint = Color.Red,
                            contentDescription = "Wrong login or password"
                        )
                        Text(
                            text = "Your Login or Password is incorrect!",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center
                        )
                        OutlinedButton(
                            onClick = tryAgain,
                            modifier = Modifier.padding(top = 50.dp)
                        ) {
                            Text(text = "Try again")
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun SimpleTabs() {
    val tabs = listOf("Day", "Month", "Year")
    var selectedTabIndex by remember { mutableIntStateOf(0) }

    Column {
        TabRow(selectedTabIndex = selectedTabIndex) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTabIndex == index,
                    onClick = { selectedTabIndex = index },
                    text = { Text(title) },
                    icon = { Icon(Icons.Default.Search, contentDescription = null) }
                )
            }
        }

        when (selectedTabIndex) {
            0 -> Text("Welcome to Home")
            1 -> Text("This is your Profile")
            2 -> Text("Adjust your Settings")
        }
    }
}



//@Preview(showBackground = true)
//@Composable
//fun CurrentUserScreenPreview() {
//    FinancesTheme {
//        CurrentUserScreen(currentUser = Users(
//            0,
//            "Morfey",
//            "currentPassword",
//            "Volodymyr",
//            "Marchuk",
//            "0674104054",
//            "vvmarchuk1984@gmail.com"
//        ), logOut = {}, tryAgain = {})
//    }
//}

@Preview(showBackground = true)
@Composable
fun CurrentUserScreenPreview() {
    FinancesTheme {
        CurrentUserScreen(currentUser = null, logOut = {}, tryAgain = {})
    }
}