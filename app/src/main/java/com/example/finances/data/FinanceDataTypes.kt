package com.example.finances.data

data class DetailedForDate(
    val sourceId: Int = 0,
    val sumForSource: Double = 0.0
)

data class DetailedBudget(
    val sourceName: String = "",
    val sumForSource: Double = 0.0
)

data class StateTypeAndPeriod(
    val type: String = "",
    val period: Int = 0
)
