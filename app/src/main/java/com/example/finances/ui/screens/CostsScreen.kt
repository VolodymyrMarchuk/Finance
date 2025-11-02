package com.example.finances.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.finances.R

import com.example.finances.data.Costs
import com.example.finances.data.SourceCosts
import com.example.finances.data.Users
import com.example.finances.ui.theme.FinancesTheme
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

@Composable
fun CostsScreen(
    sourceCosts: List<SourceCosts?>,
    showLastTenCosts: (userId: Int) -> Flow<List<Costs?>>,
    user: Users?,
    addCostsSource: (String) -> Unit,
    convertDate: (Long) -> String,
    addNewCosts: (
            user: Int,
            costsSource: Int,
            costsDate: Long,
            costsSum: Double
            ) -> Unit,
    deleteCosts: (Costs) ->Unit
) {
    var currentDate by remember { mutableLongStateOf(0) }

    val costsList by showLastTenCosts(user?.userId ?: 0).collectAsState(emptyList())
    var expanded by remember { mutableStateOf(false) }
    var costsDate by remember { mutableStateOf("") }
    var costsSource by remember { mutableIntStateOf(0)}
    var newCostsSource by remember { mutableStateOf("") }
    var costsSum by remember { mutableStateOf("") }
    var showDialog by remember { mutableStateOf(false) }
    var showCalendar by remember { mutableStateOf(false) }
    val userId = user?.userId ?: 0

    var isValidSum by remember { mutableStateOf(false) }
    val sumRegex = Regex("^\\d+(\\.\\d{2})?\$")

    Column() {
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.TopCenter
        ) {
            Card {
                Column(
                    modifier = Modifier.padding(5.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = stringResource(R.string.new_costs),
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                    if (showDialog) {
                        AddSource(
                            onDismissRequest = {
                                showDialog = false
                            },
                            onConfirmation = {
                                addCostsSource(it)
                                showDialog = false
                            }
                        )
                    }
                    OutlinedTextField(
                        value = newCostsSource,
                        readOnly = true,
                        label = {
                            Text(text = stringResource(R.string.source) + " :")
                        },
                        trailingIcon = {
                            Row {
                                Icon(
                                    imageVector = Icons.Default.Search,
                                    contentDescription = stringResource(R.string.search_costs_source),
                                    modifier = Modifier.clickable(true, onClick = {
                                        expanded = !expanded
                                    })
                                )
                                Icon(
                                    imageVector = Icons.Default.Add,
                                    contentDescription = stringResource(R.string.add_costs_source),
                                    modifier = Modifier.clickable(true, onClick = {
                                        showDialog = true
                                        newCostsSource = ""
                                    })
                                )
                            }
                            if (sourceCosts.isNotEmpty()) {
                                DropdownMenu(
                                    expanded = expanded,
                                    onDismissRequest = { expanded = false }
                                ) {
                                    for (source in sourceCosts) {
                                        DropdownMenuItem(
                                            text = { Text(text = source!!.sourceCostsName) },
                                            onClick = {
                                                expanded = false
                                                costsSource = source!!.sourceCostsId
                                                newCostsSource = source.sourceCostsName
                                            }
                                        )
                                    }
                                }

                            }
                        },
                        onValueChange = {
                            newCostsSource = it
                        }
                    )
                    if (showCalendar) {
                        ChooseDate(
                            onDateSelected = { selectedDate ->
                                if (selectedDate != null) {
                                    costsDate = convertDate(selectedDate)
                                    currentDate = selectedDate
                                } else {
                                    costsDate = "No date selected"
                                }
                                showCalendar = false
                            },
                            onDismiss = {
                                showCalendar = false
                            }
                        )
                    }
                    OutlinedTextField(
                        value = costsDate,
                        readOnly = true,
                        label = {
                            Text(text = stringResource(R.string.date) + ":")
                        },
                        onValueChange = {
                            costsDate = it
                        },
                        trailingIcon = {
                            Icon(
                                imageVector = Icons.Default.DateRange,
                                contentDescription = stringResource(R.string.calendar) + ":",
                                modifier = Modifier.clickable(enabled = true, onClick = {
                                    showCalendar = true
                                })
                            )
                        }
                    )
                    OutlinedTextField(
                        value = costsSum.toString(),
                        label = {
                            Text(text = stringResource(R.string.sum) + ":")
                        },
                        onValueChange = {
                            costsSum = it
                            isValidSum = sumRegex.matches(it)

                        },
                        isError = !isValidSum,
                        singleLine = true
                    )
                    if (isValidSum and costsDate.isNotEmpty() and newCostsSource.isNotEmpty()) {
                        OutlinedButton(
                            onClick = {
                                addNewCosts(userId, costsSource, currentDate, costsSum.toDouble())
                                newCostsSource = ""
                                costsDate = ""
                                costsSum = ""
                            },
                            modifier = Modifier.padding(top = 15.dp)
                        ) {
                            Row {
                                Icon(
                                    imageVector = Icons.Default.Add,
                                    contentDescription = stringResource(R.string.add_costs)
                                )
                                Text(text = stringResource(R.string.add))
                            }
                        }
                    } else {
                        OutlinedButton(
                            onClick = {},
                            enabled = false,
                            modifier = Modifier.padding(top = 15.dp)
                        ) {
                            Row {
                                Icon(
                                    imageVector = Icons.Default.Add,
                                    contentDescription = stringResource(R.string.add_costs)
                                )
                                Text(text = stringResource(R.string.add))
                            }
                        }
                    }

                }
            }
        }
        SeeLastTenCosts(
            costsList = costsList,
            sourceList = sourceCosts,
            convertDate = convertDate,
            deleteCosts = deleteCosts
            )
    }
}


@Composable
fun SeeLastTenCosts(
    costsList: List<Costs?>,
    sourceList: List<SourceCosts?>,
    convertDate: (Long) -> String,
    deleteCosts: (Costs) -> Unit
    ) {
    if (costsList.isNotEmpty()) {
        HorizontalDivider(
            modifier = Modifier.padding(top=20.dp)
        )
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.TopCenter
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(top = 10.dp)
            ) {
                Text(
                    text = stringResource(R.string.last_10_costs),
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold
                )
                LazyColumn(
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(costsList) { costs ->
                        Card(
                            modifier = Modifier.fillMaxWidth().padding(10.dp)
                        ) {
                            Column(
                                modifier = Modifier.fillMaxWidth().padding(5.dp)
                            ) {
                                Text(
//                                    text = costs!!.costsDate.toString(),
                                    text = convertDate(costs!!.costsDate),
                                    fontSize = 10.sp,
                                    fontStyle = FontStyle.Italic,
                                    modifier = Modifier.align(Alignment.End)
                                )
                                Row(
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    for (source in sourceList) {
                                        if (source!!.sourceCostsId == costs.costsSourceId) {
                                            Text(
                                                text = source.sourceCostsName,
                                                fontSize = 24.sp,
                                                modifier = Modifier.fillMaxSize(0.5f))
                                        }
                                    }
                                    Text(
                                        text = costs.costsSum.toString(),
                                        fontWeight = FontWeight.Bold
                                    )
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clickable(true, onClick = {deleteCosts(costs)}),
                                        contentAlignment = Alignment.BottomEnd
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Clear,
                                            contentDescription = stringResource(R.string.delete_costs),
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}


//@Preview(showBackground = true)
//@Composable
//fun LastTenPreview() {
//    FinancesTheme {
//        SeeLastTenCosts(
//            listOf(Costs(1, 2, 3, 456.45, 0),
//                Costs(2, 2, 2, 46.45, 1),
//                Costs(3, 2, 1, 4356.45, 3)
//                ),
//            listOf(SourceCosts(1, "Food"),
//                SourceCosts(2, "Sport"),
//                SourceCosts(3, "Hobby")
//                ),
//            convertDate = {lng: Long -> String.toString()},
//            deleteCosts = {}
//            )
//    }
//}


@Preview(showBackground = true)
@Composable
fun CostsScreenPreview() {
    FinancesTheme {
        CostsScreen(
            sourceCosts = listOf(
                SourceCosts(1, "Food"),
                SourceCosts(2, "Sport"),
                SourceCosts(3, "Hobby")),
            user = Users(
            0,
            "Morfey",
            "currentPassword",
            "Volodymyr",
            "Marchuk",
            "0674104054",
            "vvmarchuk1984@gmail.com"
            ),
            addCostsSource = {},
            addNewCosts = {user, costSource, costsDate, costsSum -> },
            showLastTenCosts = {userId: Int -> flowOf(listOf(
                Costs(1, 2, 3, 456.45, 0),
                Costs(2, 2, 2, 46.45, 1),
                Costs(3, 2, 1, 4356.45, 3)
            ))} ,
            convertDate = { lng: Long -> String.toString()},
            deleteCosts = {}
        )
    }
}