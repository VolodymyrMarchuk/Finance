package com.example.finances.ui.screens

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.outlined.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.ElevatedButton
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
import co.yml.charts.common.model.PlotType
import co.yml.charts.ui.piechart.charts.PieChart
import co.yml.charts.ui.piechart.models.PieChartConfig
import co.yml.charts.ui.piechart.models.PieChartData
import com.example.finances.data.Users
import com.example.finances.ui.theme.FinancesTheme
import io.github.boguszpawlowski.composecalendar.kotlinxDateTime.now
import kotlinx.datetime.LocalDate


@Composable
fun CurrentUserScreen(
    currentUser: Users?,
    costsSum: Double,
    incomeSum: Double,
    tryAgain: () -> Unit,
    goCostsScreen: () -> Unit,
    goIncomeScreen: () -> Unit,
    setData: (Int) -> Unit
) {
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
                            text = currentUser.userName.toString() + "  " + currentUser.userSurname.toString(),
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center
                        )
                        Text(
                            fontSize = 18.sp,
                            text = currentUser.userLogin.toString(),
                            modifier = Modifier.padding(start = 10.dp)
                        )
                        Text(
                            fontSize = 18.sp,
                            text = currentUser.userPhone.toString(),
                            modifier = Modifier.padding(start = 10.dp)
                        )
                        Text(
                            fontSize = 18.sp,
                            text = currentUser.userMail.toString(),
                            modifier = Modifier.padding(start = 10.dp)
                        )
                    }
                }
                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    ElevatedButton(
                        onClick = goCostsScreen,
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
                    ElevatedButton(
                        onClick = goIncomeScreen,
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
                SimpleTabs(incomeSum = incomeSum, costsSum = costsSum, setData = setData)
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
fun SimpleTabs(
    incomeSum: Double,
    costsSum: Double,
    setData: (id:Int) -> Unit
) {
    val localDate = LocalDate.now()
    val tabs = listOf("Day\n${localDate.dayOfMonth}",
        "Month\n${localDate.month}",
        "Year\n${localDate.year}")
    var selectedTabIndex by remember { mutableIntStateOf(0) }

    Column {
        TabRow(selectedTabIndex = selectedTabIndex) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTabIndex == index,
                    onClick = { selectedTabIndex = index },
                    text = {Text(title)}
                )
            }
        }
        Log.i("Charts->", "costs = $costsSum\nincom = $incomeSum")
        when (selectedTabIndex) {
            0 -> { Text("Budget in current day")
                setData(0)
                Saldo(costsSum, incomeSum)
            }
            1 -> { Text("Budget in current month")
                setData(1)
                Saldo(costsSum, incomeSum)
            }
            2 -> { Text("Budget in current year")
                setData(2)
                Saldo(costsSum, incomeSum)
            }
        }
    }
}


@Composable
fun Saldo(
    costsSum: Double,
    incomeSum: Double
) {
    val pieChartData = PieChartData(
        slices = listOf(
            PieChartData.Slice("Costs", costsSum.toFloat(), Color(0xFF009688)),
            PieChartData.Slice("Income", incomeSum.toFloat(), Color(0xFF2196F3))
        ), plotType = PlotType.Pie
    )
    val pieChartConfig = PieChartConfig(
        isAnimationEnable = true,
        showSliceLabels = true,
        animationDuration = 1500,

    )
    PieChart(
        modifier = Modifier
            .width(400.dp)
            .height(400.dp),
        pieChartData,
        pieChartConfig
    )
}


@Preview(showBackground = true)
@Composable
fun SaldoDayPreview() {
    FinancesTheme {
        Saldo(costsSum = 10.0, incomeSum = 22.0)
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
//        ), tryAgain = {}, goCostsScreen = {}, goIncomeScreen = {})
//    }
//}

//@Preview(showBackground = true)
//@Composable
//fun CurrentUserScreenPreview() {
//    FinancesTheme {
//        CurrentUserScreen(currentUser = null, logOut = {}, tryAgain = {})
//    }
//}