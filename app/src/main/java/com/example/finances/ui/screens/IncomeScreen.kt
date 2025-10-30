package com.example.finances.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import com.example.finances.data.Income
import com.example.finances.data.SourceIncome
import com.example.finances.data.Users
import com.example.finances.ui.theme.FinancesTheme
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

@Composable
fun IncomeScreen(
    sourceIncome: List<SourceIncome?>,
    showLastTenIncome: (userId: Int) -> Flow<List<Income?>>,
    user: Users?,
    addIncomeSource: (String) -> Unit,
    convertDate: (Long) -> String,
    addNewIncome: (
        user: Int,
        incomeSource: Int,
        incomeDate: Long,
        incomeSum: Double
    ) -> Unit,
    deleteIncome: (Income) ->Unit
) {
    var currentDate by remember { mutableLongStateOf(0) }

    val incomeList by showLastTenIncome(user?.userId ?: 0).collectAsState(emptyList())
    var expanded by remember { mutableStateOf(false) }
    var incomeDate by remember { mutableStateOf("") }
    var incomeSource by remember { mutableIntStateOf(0)}
    var newIncomeSource by remember { mutableStateOf("") }
    var incomeSum by remember { mutableStateOf("") }
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
                        text = stringResource(R.string.new_income),
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                    if (showDialog) {
                        AddSource(
                            onDismissRequest = {
                                showDialog = false
                            },
                            onConfirmation = {
                                addIncomeSource(it)
                                showDialog = false
                            }
                        )
                    }
                    OutlinedTextField(
                        value = newIncomeSource,
                        readOnly = true,
                        label = {
                            Text(text = stringResource(R.string.income))
                        },
                        trailingIcon = {
                            Row {
                                Icon(
                                    imageVector = Icons.Default.Search,
                                    contentDescription = stringResource(R.string.search_income_source),
                                    modifier = Modifier.clickable(true, onClick = {
                                        expanded = !expanded
                                    })
                                )
                                Icon(
                                    imageVector = Icons.Default.Add,
                                    contentDescription = stringResource(R.string.add_income_source),
                                    modifier = Modifier.clickable(true, onClick = {
                                        showDialog = true
                                        newIncomeSource = ""
                                    })
                                )
                            }
                            if (sourceIncome.isNotEmpty()) {
                                DropdownMenu(
                                    expanded = expanded,
                                    onDismissRequest = { expanded = false }
                                ) {
                                    for (source in sourceIncome) {
                                        DropdownMenuItem(
                                            text = { Text(text = source!!.sourceIncomeName) },
                                            onClick = {
                                                expanded = false
                                                incomeSource = source!!.sourceIncomeId
                                                newIncomeSource = source.sourceIncomeName
                                            }
                                        )
                                    }
                                }

                            }
                        },
                        onValueChange = {
                            newIncomeSource = it
                        }
                    )
                    if (showCalendar) {
                        ChooseDate(
                            onDateSelected = { selectedDate ->
                                if (selectedDate != null) {
                                    incomeDate = convertDate(selectedDate)
                                    currentDate = selectedDate
                                } else {
                                    incomeDate = "No date selected"
                                }
                                showCalendar = false
                            },
                            onDismiss = {
                                showCalendar = false
                            }
                        )
                    }
                    OutlinedTextField(
                        value = incomeDate,
                        readOnly = true,
                        label = {
                            Text(text = stringResource(R.string.date))
                        },
                        onValueChange = {
                            incomeDate = it
                        },
                        trailingIcon = {
                            Icon(
                                imageVector = Icons.Default.DateRange,
                                contentDescription = stringResource(R.string.calendar),
                                modifier = Modifier.clickable(enabled = true, onClick = {
                                    showCalendar = true
                                })
                            )
                        }
                    )
                    OutlinedTextField(
                        value = incomeSum.toString(),
                        label = {
                            Text(text = stringResource(R.string.sum) + ":")
                        },
                        onValueChange = {
                            incomeSum = it
                            isValidSum = sumRegex.matches(it)

                        },
                        isError = !isValidSum,
                        singleLine = true
                    )
                    if (isValidSum and incomeDate.isNotEmpty() and newIncomeSource.isNotEmpty()) {
                        OutlinedButton(
                            onClick = {
                                addNewIncome(userId, incomeSource, currentDate, incomeSum.toDouble())
                                newIncomeSource = ""
                                incomeDate = ""
                                incomeSum = ""
                            },
                            modifier = Modifier.padding(top = 15.dp)
                        ) {
                            Row {
                                Icon(
                                    imageVector = Icons.Default.Add,
                                    contentDescription = stringResource(R.string.add_income)
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
                                    contentDescription = stringResource(R.string.add_income)
                                )
                                Text(text = "Add")
                            }
                        }
                    }

                }
            }
        }
        SeeLastTenIncome(
            incomeList = incomeList,
            sourceList = sourceIncome,
            convertDate = convertDate,
            deleteIncome = deleteIncome
        )
    }
}


@Composable
fun SeeLastTenIncome(
    incomeList: List<Income?>,
    sourceList: List<SourceIncome?>,
    convertDate: (Long) -> String,
    deleteIncome: (Income) -> Unit
) {
    if (incomeList.isNotEmpty()) {
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
                    text = stringResource(R.string.last_10_income),
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold
                )
                LazyColumn(
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(incomeList) { income ->
                        Card(
                            modifier = Modifier.fillMaxWidth().padding(10.dp)
                        ) {
                            Column(
                                modifier = Modifier.fillMaxWidth().padding(5.dp)
                            ) {
                                Text(
                                    text = convertDate(income!!.incomeDate),
                                    fontSize = 10.sp,
                                    fontStyle = FontStyle.Italic,
                                    modifier = Modifier.align(Alignment.End)
                                )
                                Row(
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text(
                                        text = sourceList[income.incomeSourceId - 1]!!.sourceIncomeName,
                                        fontSize = 24.sp,
                                        modifier = Modifier.fillMaxSize(0.5f)

                                    )
                                    Text(
                                        text = income.incomeSum.toString(),
                                        fontWeight = FontWeight.Bold
                                    )
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clickable(true, onClick = {deleteIncome(income)}),
                                        contentAlignment = Alignment.BottomEnd
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Clear,
                                            contentDescription = stringResource(R.string.delete_income),
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
//        SeeLastTenIncome(
//            listOf(Income(1, 2, 3, 456.45, 0),
//                Income(2, 2, 2, 46.45, 1),
//                Income(3, 2, 1, 4356.45, 3)
//                ),
//            listOf(SourceIncome(1, "Food"),
//                SourceIncome(2, "Sport"),
//                SourceIncome(3, "Hobby")
//                ),
//            convertDate = {lng: Long -> String.toString()},
//            deleteIncome = {}
//            )
//    }
//}


@Preview(showBackground = true)
@Composable
fun IncomeScreenPreview() {
    FinancesTheme {
        IncomeScreen(
            sourceIncome = listOf(
                SourceIncome(1, "Food"),
                SourceIncome(2, "Sport"),
                SourceIncome(3, "Hobby")),
            user = Users(
                0,
                "Morfey",
                "currentPassword",
                "Volodymyr",
                "Marchuk",
                "0674104054",
                "vvmarchuk1984@gmail.com"
            ),
            addIncomeSource = {},
            addNewIncome = {user, incomeSource, incomeDate, incomeSum -> },
            showLastTenIncome = {userId: Int -> flowOf(listOf(
                Income(1, 2, 3, 456.45, 0),
                Income(2, 2, 2, 46.45, 1),
                Income(3, 2, 1, 4356.45, 3)
            ))} ,
            convertDate = { lng: Long -> String.toString()},
            deleteIncome = {}
        )
    }
}