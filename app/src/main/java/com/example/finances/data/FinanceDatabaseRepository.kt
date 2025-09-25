package com.example.finances.data

import kotlinx.coroutines.flow.Flow


interface FinanceDBRepository {
    suspend fun userRegister(userData: Users)
    suspend fun userUpdate(userData: Users)
    fun userLogin(login: String, password: String) : Flow<Users?>
}


class FinanceDatabaseRepository(private val financeDao: FinanceDao): FinanceDBRepository {
    override suspend fun userRegister(userData: Users) = financeDao.userRegistration(userData)
    override suspend fun userUpdate(userData: Users) = financeDao.userUpdateOnline(userData)
    override fun userLogin(login: String, password: String): Flow<Users?> = financeDao.logIn(login, password)
}