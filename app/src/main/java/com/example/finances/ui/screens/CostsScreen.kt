package com.example.finances.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.finances.data.Costs
import com.example.finances.data.SourceCosts
import com.example.finances.data.Users
import com.example.finances.ui.theme.FinancesTheme

@Composable
fun CostsScreen(
    sourceCosts: List<SourceCosts?>,
    user: Users?,
    addCostsSource: (String) -> Unit,
    addNewCosts: (
            user: Int,
            costsSource: Int,
            costsDate: String,
            costsSum: Float
            ) -> Unit
) {

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.TopCenter
    ) {
        var expanded by remember { mutableStateOf(false) }
        var costsDate by remember { mutableStateOf("") }
        var costsSource by remember { mutableIntStateOf(0) }
        var newCostsSource by remember { mutableStateOf("") }
        var costsSum by remember { mutableFloatStateOf(0f) }
        val userId = user?.userId ?: 0
        Card {
            Column (
                modifier = Modifier.padding(5.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "New Costs",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
                OutlinedTextField(
                    value = newCostsSource,
                    label = {
                        Text(text = "Source:")
                    },
                    trailingIcon = {
                        Row {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = "Search cost source",
                                modifier = Modifier.clickable(true, onClick = {
                                    expanded = !expanded
                                })
                            )
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "Add cost source",
                                modifier = Modifier.clickable(true, onClick = {
                                    addCostsSource(newCostsSource)
                                })
                            )
                        }
                        if(sourceCosts.isNotEmpty()) {
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
                OutlinedTextField(
                    value = costsDate,
                    label = {
                        Text(text = "Date:")
                    },
                    onValueChange = {
                        costsDate = it
                    }
                )
                OutlinedTextField(
                    value = costsSum.toString(),
                    label = {
                        Text(text = "Sum:")
                    },
                    onValueChange = {
                        costsSum = it.toFloat()
                    }
                )

                    OutlinedButton(
                        onClick = {
                            addNewCosts(userId,costsSource, costsDate, costsSum)
                        },
                        modifier = Modifier.padding(top = 15.dp)
                    ) {
                        Row {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "Add costs"
                                )
                            Text(text = "Add")
                        }
                    }

            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun CostsScreenPreview() {
    FinancesTheme {
        CostsScreen(
            sourceCosts = listOf(SourceCosts(1, "Test")),
            user = Users(
            0,
            "Morfey",
            "currentPassword",
            "Volodymyr",
            "Marchuk",
            "0674104054",
            "vvmarchuk1984@gmail.com"
        ), addCostsSource = {},
            addNewCosts = {user, costSource, costsDate, costsSum -> }
        )
    }
}