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

    @Query("SELECT * FROM users WHERE userOnline = 1")
    fun logIn() : Flow<Users>

    @Query("UPDATE users SET userOnline = 1 WHERE userLogin = :login AND userPassword = :password")
    suspend fun userOnline(login: String, password: String)

    @Query("UPDATE users SET userOnline = 0 WHERE userId = :id")
    suspend fun userOffline(id: Int)
}