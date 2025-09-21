package com.example.finances.ui

import android.app.Application
import com.example.finances.data.FinanceAppContainer
import com.example.finances.data.FinanceApplicationContainer

class FinanceApp : Application() {
    lateinit var container: FinanceAppContainer

    override fun onCreate() {
        super.onCreate()
        container = FinanceApplicationContainer(this)
    }
}