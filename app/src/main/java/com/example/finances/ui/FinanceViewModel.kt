package com.example.finances.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.finances.data.FinanceDBRepository
import com.example.finances.data.Users
import com.example.finances.ui.screens.SnackbarAction
import com.example.finances.ui.screens.SnackbarController
import com.example.finances.ui.screens.SnackbarEvent
import kotlinx.coroutines.launch

class FinanceViewModel(
    private val financeDBRepository: FinanceDBRepository
) : ViewModel() {

    val user: Users? = null

    //Registration of new user
    fun userRegistration(
        userLogin: String,
        userPassword: String,
        userName: String,
        userSurname: String,
        userPhone: String,
        userMail: String
    ) = viewModelScope.launch {
        //Must check if new user already exists
        val insertItem = user?.copy(
            userLogin = userLogin,
            userPassword = userPassword,
            userName = userName,
            userSurname = userSurname,
            userPhone = userPhone,
            userMail = userMail
        ) ?: Users(
            userLogin = userLogin,
            userPassword = userPassword,
            userName = userName,
            userSurname = userSurname,
            userPhone = userPhone,
            userMail = userMail
        )
        try {
            financeDBRepository.userRegister(insertItem)
            SnackbarController.sendEvent(
                event = SnackbarEvent(
                    message = "Success: You are registered!"
                )
            )
        } catch (e: Exception) {
            Log.i("Insert user -> ", e.message.toString())
            SnackbarController.sendEvent(
                event = SnackbarEvent(
                    message = "The User or Mail exists!",
                    action = SnackbarAction(
                        name = "Try again",
                        action = {
                            //What we have to do
                            SnackbarController.sendEvent(
                                event = SnackbarEvent(message = "Close")
                            )
                        }
                    )
                )
            )
        }
    }

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