package com.example.finances.data

import android.content.Context


interface FinanceAppContainer {
    val financeDatabaseRepository: FinanceDatabaseRepository
}


class FinanceApplicationContainer(private val context: Context) : FinanceAppContainer {
    override val financeDatabaseRepository: FinanceDatabaseRepository by lazy {
        FinanceDatabaseRepository(financeDao = FinanceDatabase.getDatabase(context).financeDao())
    }
}