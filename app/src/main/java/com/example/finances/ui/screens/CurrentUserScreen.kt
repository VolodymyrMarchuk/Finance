package com.example.finances.ui.screens

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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import co.yml.charts.common.model.PlotType
import co.yml.charts.ui.piechart.charts.PieChart
import co.yml.charts.ui.piechart.models.PieChartConfig
import co.yml.charts.ui.piechart.models.PieChartData
import com.example.finances.R
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
    setData: (Int) -> Unit,
    onBudgetSliceClick: (String, Int) -> Unit
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
                            .fillMaxWidth(0.5f)
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
                                text = stringResource(R.string.costs),
                                fontSize = 20.sp,
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
                                text = stringResource(R.string.income),
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = null
                            )
                        }
                    }
                }
                SimpleTabs(
                    incomeSum = incomeSum,
                    costsSum = costsSum,
                    setData = setData,
                    onBudgetSliceClick = onBudgetSliceClick
                    )
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
    setData: (id:Int) -> Unit,
    onBudgetSliceClick: (String, Int) -> Unit
) {
    val localDate = LocalDate.now()
    val tabs = listOf(stringResource(R.string.day)+"\n${localDate.dayOfMonth}",
        stringResource(R.string.month)+"\n${localDate.month}",
        stringResource(R.string.year)+"\n${localDate.year}")
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
        when (selectedTabIndex) {
            0 -> {
                Text(stringResource(R.string.budget_current) +
                        stringResource(R.string.day) +
                        " -> " +String.format("%.2f", (incomeSum-costsSum)) + " UAH")
                setData(0)
                Saldo(costsSum, incomeSum, onBudgetSliceClick = {
                    onBudgetSliceClick(it, 0)
                })
            }
            1 -> {
                Text(stringResource(R.string.budget_current) +
                        stringResource(R.string.month) +
                        " -> " +String.format("%.2f", (incomeSum-costsSum)) + " UAH")
                setData(1)
                Saldo(costsSum, incomeSum, onBudgetSliceClick = {
                    onBudgetSliceClick(it, 1)
                })
            }
            2 -> {
                Text(stringResource(R.string.budget_current) +
                        stringResource(R.string.year) +
                        " -> " +String.format("%.2f", (incomeSum-costsSum)) + " UAH")
                setData(2)
                Saldo(costsSum, incomeSum, onBudgetSliceClick = {
                    onBudgetSliceClick(it, 2)
                })
            }
        }
    }
}


@Composable
fun Saldo(
    costsSum: Double,
    incomeSum: Double,
    onBudgetSliceClick: (String) -> Unit
) {
    val pieChartData = PieChartData(
        slices = listOf(
            PieChartData.Slice(stringResource(R.string.costs), costsSum.toFloat(), Color(0xFF009688)),
            PieChartData.Slice(stringResource(R.string.income), incomeSum.toFloat(), Color(0xFF2196F3))
        ), plotType = PlotType.Pie
    )
    val pieChartConfig = PieChartConfig(
        isAnimationEnable = true,
        showSliceLabels = true,
        animationDuration = 1500,
        backgroundColor = Color.Transparent

    )
    PieChart(
        modifier = Modifier
            .width(400.dp)
            .height(400.dp),
        pieChartData,
        pieChartConfig,
        onSliceClick = {
            onBudgetSliceClick(it.label)
        }
    )
}


//@Preview(showBackground = true)
//@Composable
//fun SaldoDayPreview() {
//    FinancesTheme {
//        Saldo(costsSum = 10.0, incomeSum = 22.0, onBudgetSliceClick = {})
//    }
//}


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

@Preview(showBackground = true)
@Composable
fun CurrentUserScreenPreview() {
    FinancesTheme {
        CurrentUserScreen(
            currentUser = Users(
            0,
            "Morfey",
            "currentPassword",
            "Volodymyr",
            "Marchuk",
            "0674104054",
            "vvmarchuk1984@gmail.com"
        ),
            costsSum = 4500.00,
            incomeSum = 15000.00,
            tryAgain = {},
            goCostsScreen = {},
            goIncomeScreen = {},
            setData = {},
            onBudgetSliceClick = {String, Int ->}
            )
    }
}