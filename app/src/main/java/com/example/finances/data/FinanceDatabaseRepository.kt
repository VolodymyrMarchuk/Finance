package com.example.finances.data

import kotlinx.coroutines.flow.Flow


interface FinanceDBRepository {
    suspend fun newCostsSource(newCostsSource: SourceCosts)
    fun allCostsSources() : Flow<List<SourceCosts?>>
    fun tenCosts(userId: Int) : Flow<List<Costs?>>
    suspend fun addCosts(newCosts: Costs)
    suspend fun delCosts(costs: Costs)

    suspend fun userRegister(userData: Users)
    fun userLogin() : Flow<Users?>
    suspend fun userOnline(login: String, password: String)
    suspend fun userOffline(id: Int)
    suspend fun userUpdate(id: Int, name: String, surname: String, phone: String, mail: String, password: String)
}


class FinanceDatabaseRepository(private val financeDao: FinanceDao): FinanceDBRepository {
    override suspend fun newCostsSource(newCostsSource: SourceCosts) = financeDao.addCostsSource(newCostsSource)
    override fun allCostsSources(): Flow<List<SourceCosts?>> = financeDao.showCostsSources()
    override fun tenCosts(userId: Int): Flow<List<Costs?>> = financeDao.showTenCosts(userId)
    override suspend fun addCosts(newCosts: Costs) = financeDao.addCosts(newCosts)
    override suspend fun delCosts(costs: Costs) = financeDao.deleteCosts(costs)

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
}