package com.example.finances.data

import kotlinx.coroutines.flow.Flow


interface FinanceDBRepository {
    suspend fun userRegister(userData: Users)
    fun userLogin() : Flow<Users?>
    suspend fun userOnline(login: String, password: String)
    suspend fun userOffline(id: Int)
}


class FinanceDatabaseRepository(private val financeDao: FinanceDao): FinanceDBRepository {
    override suspend fun userRegister(userData: Users) = financeDao.userRegistration(userData)
    override fun userLogin(): Flow<Users?> = financeDao.logIn()
    override suspend fun userOnline(login: String, password: String) = financeDao.userOnline(login, password)
    override suspend fun userOffline(id: Int) = financeDao.userOffline(id)
}