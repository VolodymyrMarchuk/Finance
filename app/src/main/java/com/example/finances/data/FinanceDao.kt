package com.example.finances.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow


@Dao
interface FinanceDao {
    @Insert(entity = Users::class, onConflict = OnConflictStrategy.FAIL)
    suspend fun userRegistration(userData: Users)
    @Insert(entity = SourceCosts::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun addCostsSource(newCostsSource: SourceCosts)
    @Insert(entity = Costs::class, onConflict = OnConflictStrategy.IGNORE)
    suspend fun addCosts(newCosts: Costs)

    @Query("SELECT * FROM users WHERE userOnline = 1")
    fun logIn() : Flow<Users>
    @Query("SELECT * FROM sourceCosts")
    fun showCostsSources() : Flow<List<SourceCosts?>>

    @Query("UPDATE users SET userOnline = 1 WHERE userLogin = :login AND userPassword = :password")
    suspend fun userOnline(login: String, password: String)

    @Query("UPDATE users SET userOnline = 0 WHERE userId = :id")
    suspend fun userOffline(id: Int)
    @Query("UPDATE users SET userName = :name, userSurname =:surname, userPhone =:phone, userMail =:mail, userPassword =:password WHERE userId = :id")
    suspend fun userUpdate(id: Int, name: String, surname: String, phone: String, mail: String, password: String)
}