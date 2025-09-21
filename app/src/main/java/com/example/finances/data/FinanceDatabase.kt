package com.example.finances.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(
    entities = [
        Users::class,
        Income::class,
        Costs::class,
        SourceIncome::class,
        SourceCosts::class],
    version = 1,
    exportSchema = false
    )
abstract class FinanceDatabase : RoomDatabase() {
    abstract fun financeDao() : FinanceDao

    companion object {
        @Volatile
        private var Instance: FinanceDatabase? = null
        fun getDatabase(context: Context) : FinanceDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(
                    context,
                    FinanceDatabase::class.java,
                    "finance")
                    .build()
                    .also { Instance = it }
            }
        }
    }
}