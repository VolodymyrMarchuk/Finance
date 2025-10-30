package com.example.finances.ui

import android.content.Context
import android.util.Log
import androidx.compose.runtime.asDoubleState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.finances.R
import com.example.finances.data.Costs
import com.example.finances.data.DetailedForDate
import com.example.finances.data.FinanceDBRepository
import com.example.finances.data.Income
import com.example.finances.data.SourceCosts
import com.example.finances.data.SourceIncome
import com.example.finances.data.StateTypeAndPeriod
import com.example.finances.data.Users
import com.example.finances.ui.screens.SnackbarAction
import com.example.finances.ui.screens.SnackbarController
import com.example.finances.ui.screens.SnackbarEvent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.ZoneId
import java.util.Date
import java.util.Locale


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
    var sourceCosts: SourceCosts? = null
    var costs: Costs? = null
    var sourceIncome: SourceIncome? = null
    var income: Income? = null

    private val _currentuser = MutableStateFlow(UserCurrent())
    val currentUser = _currentuser.asStateFlow()

    private var _typeAndPeriod = MutableStateFlow(StateTypeAndPeriod())
    val typeAndPeriod = _typeAndPeriod.asStateFlow()

    private val today = LocalDate.now()

    //Convert Long->Date to String->Date ----------------------------------------------------------
    fun convertDateLongToString(selectedDate: Long): String {
        val date = Date(selectedDate)
        val formattedDate = SimpleDateFormat("dd, MM, yyyy", Locale.getDefault()).format(date)
        return formattedDate
    }

    fun convertLocalDateToLong(selectionDate: LocalDate): Long {
        val zoneId = ZoneId.of("UTC")
        val epochMillis = selectionDate.atStartOfDay(zoneId).toInstant().toEpochMilli()
        return epochMillis
    }

    private fun dateFrom(periodId: Int) = when (periodId) {
        1 -> {
            val firstDayMonth = today.withDayOfMonth(1)
            convertLocalDateToLong(firstDayMonth)
        }
        2 -> {
            val firstDayYear = today.withDayOfYear(1)
            convertLocalDateToLong(firstDayYear)
        }
        else -> {
            convertLocalDateToLong(today)
        }
    }

    private fun dateTill(periodId: Int) = when (periodId) {
        1 -> {
            val lastDayMonth = today.withDayOfMonth(today.lengthOfMonth())
            convertLocalDateToLong(lastDayMonth)
        }
        2 -> {
            val lastDayYear = today.withDayOfYear(today.lengthOfYear())
            convertLocalDateToLong(lastDayYear)
        }
        else -> {
            convertLocalDateToLong(today)
        }
    }

    fun saveTypeAndPeriod(type: String, period: Int) {
        _typeAndPeriod.update { newState->
            newState.copy(
                type = type,
                period = period
            )
        }
    }

    //---------------------------------------------------------USER--------------------------------
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

    fun verifyUser(
        userLogin: String,
        userPassword: String,
    ) {
        Log.i("verifyUser user ->", "Start")
        _currentuser.update { user->
            user.copy(
                userLogin = userLogin,
                userPassword = userPassword
            )
        }
        userOnline(userLogin = userLogin, userPassword = userPassword)
    }

    fun userLogin() : Flow<Users?> {
        val currentUser = financeDBRepository.userLogin()
        return currentUser
    }
    private fun userOnline(
        userLogin: String,
        userPassword: String,
    ) {
        viewModelScope.launch {
            financeDBRepository.userOnline(userLogin, userPassword)
        }
        Log.i("Online user ->", "Finish")
    }
    fun userOffline(id: Int) {
        viewModelScope.launch {
            financeDBRepository.userOffline(id)
        }
    }
    fun userUpdate(
        id: Int,
        name: String,
        surname: String,
        phone: String,
        mail: String,
        password: String) = viewModelScope.launch {
        financeDBRepository.userUpdate(id, name, surname, phone, mail, password)
    }


    // -------------------------------------------------------- COSTS -----------------------------
    private val _totalCosts = mutableStateOf(0.0)
    val totalCosts = _totalCosts.asDoubleState()
    fun addCosts(
        userId: Int,
        costsSource: Int,
        costsDate: Long,
        costsSum: Double
    ) = viewModelScope.launch {
        val insertItem = costs?.copy(
            costsUserId = userId,
            costsSourceId = costsSource,
            costsDate = costsDate,
            costsSum = costsSum
        ) ?: Costs(
            costsUserId = userId,
            costsSourceId = costsSource,
            costsDate = costsDate,
            costsSum = costsSum
        )
        try {
            financeDBRepository.addCosts(insertItem)
            costs = null
        } catch (e: Exception) {
            Log.i("Insert costs -> ", e.message.toString())
        }
    }
    fun showLastTenCosts(userId: Int) : Flow<List<Costs?>> {
        val lastTenCosts = financeDBRepository.tenCosts(userId)
        return lastTenCosts
    }
    fun showForDateCosts(userId: Int, periodId: Int) {
        val dateFrom = dateFrom(periodId)
        val dateTill = dateTill(periodId)
        viewModelScope.launch {
            _totalCosts.value = financeDBRepository.forDateCosts(userId, dateFrom, dateTill)
        }
    }
    fun addNewCostsSource(newCostsSource: String) = viewModelScope.launch {
        val insertItem = sourceCosts?.copy(
            sourceCostsName = newCostsSource.replaceFirstChar { it.uppercase() }
        ) ?: SourceCosts(sourceCostsName = newCostsSource.replaceFirstChar { it.uppercase() })

        try {
            financeDBRepository.newCostsSource(insertItem)
            sourceCosts = null
        } catch (e: Exception) {
            Log.i("Insert sourceCosts -> ", e.message.toString())
        }
    }
    fun showAllCostsSources() : Flow<List<SourceCosts?>> {
        val listCostsSource = financeDBRepository.allCostsSources()
        return listCostsSource
    }
    fun deleteCosts(costs: Costs) = viewModelScope.launch {
        financeDBRepository.delCosts(costs)
    }

    // --------------------------------------------------------INCOME-------------------------------
    private val _totalIncome = mutableStateOf(0.0)
    val totalIncome = _totalIncome.asDoubleState()
    fun addIncome(
        userId: Int,
        incomeSource: Int,
        incomeDate: Long,
        incomeSum: Double
    ) = viewModelScope.launch {
        val insertItem = income?.copy(
            incomeUserId = userId,
            incomeSourceId = incomeSource,
            incomeDate = incomeDate,
            incomeSum = incomeSum
        ) ?: Income(
            incomeUserId = userId,
            incomeSourceId = incomeSource,
            incomeDate = incomeDate,
            incomeSum = incomeSum
        )
        try {
            financeDBRepository.addIncome(insertItem)
            income = null
        } catch (e: Exception) {
            Log.i("Insert income -> ", e.message.toString())
        }
    }
    fun showLastTenIncome(userId: Int) : Flow<List<Income?>> {
        val lastTenIncome = financeDBRepository.tenIncome(userId)
        return lastTenIncome
    }
    fun showForDateIncome(userId: Int, periodId: Int) {
        val dateFrom = dateFrom(periodId)
        val dateTill = dateTill(periodId)
        viewModelScope.launch {
            _totalIncome.value = financeDBRepository.forDateIncome(userId, dateFrom, dateTill)
        }
    }
    fun addNewIncomeSource(newIncomeSource: String) = viewModelScope.launch {
        val insertItem = sourceIncome?.copy(
            sourceIncomeName = newIncomeSource.replaceFirstChar { it.uppercase() }
        ) ?: SourceIncome(sourceIncomeName = newIncomeSource.replaceFirstChar { it.uppercase() })

        try {
            financeDBRepository.newIncomeSource(insertItem)
            sourceIncome = null
        } catch (e: Exception) {
            Log.i("Insert sourceIncome -> ", e.message.toString())
        }
    }
    fun showAllIncomeSources() : Flow<List<SourceIncome?>> {
        val listIncomeSource = financeDBRepository.allIncomeSources()
        return listIncomeSource
    }
    fun deleteIncome(income: Income) = viewModelScope.launch {
        financeDBRepository.delIncome(income)
    }

    // -------------------------------------------------------Budget detailed-----------------------
    fun showBudgetDetailed(userId: Int, type: String, periodId: Int) : Flow<List<DetailedForDate>> {
        val dateFrom = dateFrom(periodId)
        val dateTill = dateTill(periodId)
        var detailedForDate: Flow<List<DetailedForDate>> = flowOf(listOf())
        when (type) {
            "Costs" -> { detailedForDate = financeDBRepository.forDateCostsDetailed(userId, dateFrom, dateTill) }
            "Витрати" -> { detailedForDate = financeDBRepository.forDateCostsDetailed(userId, dateFrom, dateTill) }
            "Income" -> { detailedForDate = financeDBRepository.forDateIncomeDetailed(userId, dateFrom, dateTill) }
            "Доходи" -> { detailedForDate = financeDBRepository.forDateIncomeDetailed(userId, dateFrom, dateTill) }
        }
        return detailedForDate
    }
    // ---------------------------------------------------------------------------------------------


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