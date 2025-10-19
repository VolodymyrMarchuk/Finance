package com.example.finances.data

import kotlinx.coroutines.flow.Flow


interface FinanceDBRepository {

    //------------------------------------------------------USER-----------------------------------
    suspend fun userRegister(userData: Users)
    fun userLogin() : Flow<Users?>
    suspend fun userOnline(login: String, password: String)
    suspend fun userOffline(id: Int)
    suspend fun userUpdate(id: Int, name: String, surname: String, phone: String, mail: String, password: String)

    //------------------------------------------------------COSTS----------------------------------
    suspend fun newCostsSource(newCostsSource: SourceCosts)
    fun allCostsSources() : Flow<List<SourceCosts?>>
    fun tenCosts(userId: Int) : Flow<List<Costs?>>
    suspend fun forDateCosts(userId: Int, date: Long) : Double
    suspend fun addCosts(newCosts: Costs)
    suspend fun delCosts(costs: Costs)

    //------------------------------------------------------INCOME---------------------------------
    suspend fun newIncomeSource(newIncomeSource: SourceIncome)
    fun allIncomeSources() : Flow<List<SourceIncome?>>
    fun tenIncome(userId: Int) : Flow<List<Income?>>
    suspend fun forDateIncome(userId: Int, date: Long) : Double
    suspend fun addIncome(newIncome: Income)
    suspend fun delIncome(income: Income)
}


class FinanceDatabaseRepository(private val financeDao: FinanceDao): FinanceDBRepository {

    //------------------------------------------------------USER-----------------------------------
    override suspend fun userRegister(userData: Users) = financeDao.userRegistration(userData)
    override fun userLogin(): Flow<Users?> = financeDao.logIn()
    override suspend fun userOnline(login: String, password: String) = financeDao.userOnline(login, password)
    override suspend fun userOffline(id: Int) = financeDao.userOffline(id)
    override suspend fun userUpdate(
        id: Int,
        name: String,
        surname: String,
        phone: String,
        mail: String,
        password: String
    ) = financeDao.userUpdate(id, name, surname, phone, mail, password)
    //------------------------------------------------------COSTS----------------------------------
    override suspend fun newCostsSource(newCostsSource: SourceCosts) = financeDao.addCostsSource(newCostsSource)
    override fun allCostsSources(): Flow<List<SourceCosts?>> = financeDao.showCostsSources()
    override fun tenCosts(userId: Int): Flow<List<Costs?>> = financeDao.showTenCosts(userId)
    override suspend fun forDateCosts(userId: Int, date: Long): Double = financeDao.showCostsForDate(userId, date) ?: 0.0
    override suspend fun addCosts(newCosts: Costs) = financeDao.addCosts(newCosts)
    override suspend fun delCosts(costs: Costs) = financeDao.deleteCosts(costs)

    //------------------------------------------------------INCOME---------------------------------
    override suspend fun newIncomeSource(newIncomeSource: SourceIncome) = financeDao.addIncomeSource(newIncomeSource)
    override fun allIncomeSources() : Flow<List<SourceIncome?>> = financeDao.showIncomeSources()
    override fun tenIncome(userId: Int) : Flow<List<Income?>> = financeDao.showTenIncome(userId)
    override suspend fun forDateIncome(userId: Int, date: Long) : Double = financeDao.showIncomeForDate(userId, date) ?: 0.0
    override suspend fun addIncome(newIncome: Income) = financeDao.addIncome(newIncome)
    override suspend fun delIncome(income: Income) = financeDao.deleteIncome(income)
}