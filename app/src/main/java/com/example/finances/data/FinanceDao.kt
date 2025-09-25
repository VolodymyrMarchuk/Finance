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

    @Insert(entity = Users::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun userUpdateOnline(userData: Users)

    @Query("SELECT * FROM users WHERE userLogin = :login AND userPassword = :password")
    fun logIn(login: String, password: String) : Flow<Users>
}