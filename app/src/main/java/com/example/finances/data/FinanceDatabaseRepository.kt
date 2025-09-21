package com.example.finances.data


interface FinanceDBRepository {
}


class FinanceDatabaseRepository(private val financeDao: FinanceDao): FinanceDBRepository {

}