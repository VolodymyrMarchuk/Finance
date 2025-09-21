package com.example.finances.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.finances.data.FinanceDBRepository

class FinanceViewModel(
    private val financeDBRepository: FinanceDBRepository
) : ViewModel() {

    //viewModel

    companion object {
        val factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as FinanceApp)
                val financeDBRepository = application.container.financeDatabaseRepository
                FinanceViewModel(financeDBRepository = financeDBRepository)
            }
        }
    }
}