package com.example.finances.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow


@Dao
interface FinanceDao {
    //----------------------------------------------------USER-------------------------------------
    @Insert(entity = Users::class, onConflict = OnConflictStrategy.FAIL)
    suspend fun userRegistration(userData: Users)
    @Query("SELECT * FROM users WHERE userOnline = 1")
    fun logIn() : Flow<Users>
    @Query("UPDATE users SET userOnline = 1 WHERE userLogin = :login AND userPassword = :password")
    suspend fun userOnline(login: String, password: String)
    @Query("UPDATE users SET userOnline = 0 WHERE userId = :id")
    suspend fun userOffline(id: Int)
    @Query("UPDATE users SET userName = :name, userSurname =:surname, userPhone =:phone, userMail =:mail, userPassword =:password WHERE userId = :id")
    suspend fun userUpdate(id: Int, name: String, surname: String, phone: String, mail: String, password: String)

    //----------------------------------------------------COSTS------------------------------------
    @Insert(entity = SourceCosts::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun addCostsSource(newCostsSource: SourceCosts)
    @Insert(entity = Costs::class, onConflict = OnConflictStrategy.IGNORE)
    suspend fun addCosts(newCosts: Costs)
    @Query("SELECT * FROM sourceCosts")
    fun showCostsSources() : Flow<List<SourceCosts?>>
    @Query("SELECT * FROM costs WHERE costsUserId=:userId ORDER BY costsDate DESC LIMIT 10")
    fun showTenCosts(userId: Int) : Flow<List<Costs?>>
    @Query("SELECT SUM(costsSum) FROM costs WHERE (costsUserId=:userId AND costsDate=:date)")
    suspend fun showCostsForDate(userId: Int, date: Long) : Double?
    @Delete(entity = Costs::class)
    suspend fun deleteCosts(costs: Costs)

    //----------------------------------------------------INCOME-----------------------------------
    @Insert(entity = SourceIncome::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun addIncomeSource(newIncomeSource: SourceIncome)
    @Insert(entity = Income::class, onConflict = OnConflictStrategy.IGNORE)
    suspend fun addIncome(newIncome: Income)
    @Query("SELECT * FROM sourceIncome")
    fun showIncomeSources() : Flow<List<SourceIncome?>>
    @Query("SELECT * FROM income WHERE incomeUserId=:userId ORDER BY incomeDate DESC LIMIT 10")
    fun showTenIncome(userId: Int) : Flow<List<Income?>>
    @Query("SELECT SUM(incomeSum) FROM income WHERE (incomeUserId=:userId AND incomeDate=:date)")
    suspend fun showIncomeForDate(userId: Int, date: Long) : Double?
    @Delete(entity = Income::class)
    suspend fun deleteIncome(income: Income)
}