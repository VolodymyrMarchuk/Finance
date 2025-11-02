package com.example.finances.ui.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import co.yml.charts.common.model.PlotType
import co.yml.charts.ui.piechart.charts.PieChart
import co.yml.charts.ui.piechart.models.PieChartConfig
import co.yml.charts.ui.piechart.models.PieChartData
import com.example.finances.R
import com.example.finances.data.DetailedForDate
import com.example.finances.data.SourceCosts
import com.example.finances.data.SourceIncome
import com.example.finances.ui.theme.FinancesTheme
import io.github.boguszpawlowski.composecalendar.kotlinxDateTime.now
import kotlinx.datetime.LocalDate
import kotlin.random.Random

@Composable
fun DetailedScreen(
    detailedList: List<DetailedForDate>,
    sourceCosts: List<SourceCosts?>,
    sourceIncome: List<SourceIncome?>,
    type: String,
    period: Int,
    saveTypeAndPeriod: (String, Int) -> Unit
) {
    Column {

        DetailedSimpleTabs(
            type = type,
            period = period,
            sourceCosts = sourceCosts,
            sourceIncome = sourceIncome,
            detailedList = detailedList,
            saveTypeAndPeriod = saveTypeAndPeriod
            )
    }
}


@Composable
fun DetailedSimpleTabs(
    type: String,
    period: Int,
    detailedList: List<DetailedForDate>,
    sourceCosts: List<SourceCosts?>,
    sourceIncome: List<SourceIncome?>,
    saveTypeAndPeriod: (String, Int) -> Unit
) {
    val localDate = LocalDate.now()
    val tabs = listOf(stringResource(R.string.day) + " \n${localDate.dayOfMonth}",
        stringResource(R.string.month) + " \n${localDate.month}",
        stringResource(R.string.year) + " \n${localDate.year}")
    var selectedTabIndex by remember { mutableIntStateOf(period) }

    Column {
        TabRow(selectedTabIndex = selectedTabIndex) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTabIndex == index,
                    onClick = { selectedTabIndex = index },
                    text = { Text(title) }
                )
            }
        }
        when (selectedTabIndex) {
            0 -> {
                saveTypeAndPeriod(type, 0)
                Text(stringResource(R.string.budget_current_detailed) + " " + stringResource(R.string.day))
                if (detailedList.isNotEmpty()) {
                    BudgetDetailed(
                        detailedList = detailedList,
                        sourceCosts = sourceCosts,
                        sourceIncome = sourceIncome,
                        type = type
                    )
                }
            }

            1 -> {
                saveTypeAndPeriod(type, 1)
                Text(stringResource(R.string.budget_current_detailed) + " " + stringResource(R.string.month))
                if (detailedList.isNotEmpty()) {
                    BudgetDetailed(
                        detailedList = detailedList,
                        sourceCosts = sourceCosts,
                        sourceIncome = sourceIncome,
                        type = type
                    )
                }
            }

            2 -> {
                saveTypeAndPeriod(type, 2)
                Text(stringResource(R.string.budget_current_detailed) + " " + stringResource(R.string.year))
                if (detailedList.isNotEmpty()) {
                    BudgetDetailed(
                        detailedList = detailedList,
                        sourceCosts = sourceCosts,
                        sourceIncome = sourceIncome,
                        type = type
                    )
                }
            }
        }
    }
}


@SuppressLint("DefaultLocale")
@Composable
fun BudgetDetailed(
    detailedList: List<DetailedForDate>,
    sourceCosts: List<SourceCosts?>,
    sourceIncome: List<SourceIncome?>,
    type: String
) {
    val setDetailedSource = mutableMapOf(0 to "")
    if (type == stringResource(R.string.costs)) {
        for (source in sourceCosts) {
            setDetailedSource[source!!.sourceCostsId] = source.sourceCostsName
        }
    } else {
        for (source in sourceIncome) {
            setDetailedSource[source!!.sourceIncomeId] = source.sourceIncomeName
        }
    }

    val slices = mutableListOf<PieChartData.Slice>()
    for (i in detailedList) {
        val red = Random.nextInt(100, 256)
        val green = Random.nextInt(100, 256)
        val blue = Random.nextInt(100, 256)

        setDetailedSource[i.sourceId]?.let {
            slices.add(PieChartData.Slice(
                label = it,
                value = i.sumForSource.toFloat(),
                Color(red, green, blue)
            ))
        }

    }
    val pieChartData = PieChartData(
        slices = slices,
        plotType = PlotType.Pie
    )
    val pieChartConfig = PieChartConfig(
        isAnimationEnable = true,
        showSliceLabels = true,
        animationDuration = 1500,
        backgroundColor = Color.Transparent,
    )

    val sumTotal = detailedList.sumOf { (sourceId, sumForSource) -> sumForSource }

    Column {
        Box(
            modifier = Modifier
                .fillMaxHeight(0.3f)
                .padding(10.dp)
        ) {
            Column {
                Text(
                    text = "$type = " + String.format("%.2f", sumTotal),
                    fontWeight = FontWeight.Bold
                    )
                LazyColumn {
                    items(detailedList) {
                        Row(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "${setDetailedSource[it.sourceId]}",
                                modifier = Modifier
                                    .fillMaxWidth(0.3f)
                                    .padding(start = 15.dp)
                                )
                            Text(text = String.format("%.2f", it.sumForSource) + "  (" + String.format("%.1f%%", (it.sumForSource/sumTotal)*100) + ")")
                        }
                    }
                }
            }
        }
        Box {
            PieChart(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(5.dp),
                pieChartData,
                pieChartConfig
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun DetailedScreenPreview() {
    FinancesTheme {
        BudgetDetailed(
            detailedList = listOf(
                DetailedForDate(1,15.00),
                DetailedForDate(2,30.00),
                DetailedForDate(3,20.00)),
            sourceCosts = listOf(
                SourceCosts(1, "Food"),
                SourceCosts(2, "Sport"),
                SourceCosts(3, "Hobby")),
            sourceIncome = listOf(
                SourceIncome(1, "Food"),
                SourceIncome(2, "Sport"),
                SourceIncome(3, "Hobby")
            ),
            type = "Income"
            )
    }
}


//@Preview(showBackground = true)
//@Composable
//fun DetailedScreenPreview() {
//    FinancesTheme {
//        DetailedScreen(detailedList = listOf(
//            DetailedForDate(1, 10.0),
//            DetailedForDate(2, 15.0),
//            DetailedForDate(3, 30.0)),
//            type = "", period = 0,
//            sourceCosts = listOf(SourceCosts(1, "Food"),
//                SourceCosts(2, "Sport"),
//                SourceCosts(3, "Hobby")),
//            sourceIncome = listOf(SourceIncome(1, "Food"),
//                SourceIncome(2, "Sport"),
//                SourceIncome(3, "Hobby")))
//    }
//}