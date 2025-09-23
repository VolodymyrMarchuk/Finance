package com.example.finances.data


interface FinanceDBRepository {
    suspend fun userRegister(userData: Users)
}


class FinanceDatabaseRepository(private val financeDao: FinanceDao): FinanceDBRepository {
    override suspend fun userRegister(userData: Users) = financeDao.userRegistration(userData)
}