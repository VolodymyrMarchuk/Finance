package com.example.finances.ui

import android.util.Log
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.room.ColumnInfo
import androidx.room.PrimaryKey
import com.example.finances.data.FinanceDBRepository
import com.example.finances.data.Users
import com.example.finances.ui.screens.SnackbarAction
import com.example.finances.ui.screens.SnackbarController
import com.example.finances.ui.screens.SnackbarEvent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlin.String


data class UserCurrent(
    val userId: Int? = null,
    val userLogin: String? = null,
    val userPassword: String? = null,
    val userName: String? = null,
    val userSurname: String? = null,
    val userPhone: String? = null,
    val userMail: String? = null,
    val userOnline: Boolean? = false
)


class FinanceViewModel(
    private val financeDBRepository: FinanceDBRepository
) : ViewModel() {

    var user: Users? = null

    private val _currentuser = MutableStateFlow(UserCurrent())
    val currentUser = _currentuser.asStateFlow()

    //Update used in Database - online

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
            user = null
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

    fun updateCurrentUser(
        userLogin: String,
        userPassword: String,
    ) {
        _currentuser.update { user->
            user.copy(
                userLogin = userLogin,
                userPassword = userPassword
            )
        }
    }

    fun userLogin(
        userLogin: String,
        userPassword: String,
    ) : Flow<Users?> {
        val userLog = financeDBRepository.userLogin(userLogin, userPassword)

        return userLog
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