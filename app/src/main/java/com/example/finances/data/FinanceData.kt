package com.example.finances.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class Users(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "userId") val userId: Int = 0,
    @ColumnInfo(name = "userLogin") val userLogin: String,

    //Should add for password field -> encrypt before SAVING and decrypt when READING
    @ColumnInfo(name = "userPassword") val userPassword: String,

    @ColumnInfo(name = "userName") val userName: String,
    @ColumnInfo(name = "userSurname") val userSurname: String,
    @ColumnInfo(name = "userPhone") val userPhone: String,
    @ColumnInfo(name = "userMail") val userMail: String
)


@Entity(tableName = "sourceIncome")
data class SourceIncome(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "sourceIncomeId") val sourceIncomeId: Int = 0,
    @ColumnInfo(name = "sourceIncomeName") val sourceIncomeName: String
)


@Entity(tableName = "sourceCosts")
data class SourceCosts(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "sourceCostsId") val sourceCostsId: Int = 0,
    @ColumnInfo(name = "sourceCostsName") val sourceCostsName: String
)


@Entity(
    tableName = "income",
    foreignKeys = [
        ForeignKey(
            entity = Users::class,
            parentColumns = ["userId"],
            childColumns = ["incomeUserId"]
        ),
        ForeignKey(
            entity = SourceIncome::class,
            parentColumns = ["sourceIncomeId"],
            childColumns = ["incomeSourceId"]
        )
    ]
)
data class Income(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "incomeId") val incomeId: Int = 0,
    @ColumnInfo(name = "incomeUserId") val incomeUserId: Int,
    @ColumnInfo(name = "incomeSourceId") val incomeSourceId: Int,
    @ColumnInfo(name = "incomeSum") val incomeSum : Float,
    @ColumnInfo(name = "incomeDate") val incomeDate : String

)


@Entity(
    tableName = "costs",
    foreignKeys = [
        ForeignKey(
            entity = Users::class,
            parentColumns = ["userId"],
            childColumns = ["costsUserId"]
        ),
        ForeignKey(
            entity = SourceIncome::class,
            parentColumns = ["sourceIncomeId"],
            childColumns = ["costsSourceId"]
        )
    ]
)
data class Costs(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "costsId") val costsId: Int = 0,
    @ColumnInfo(name = "costsUserId") val costsUserId: Int,
    @ColumnInfo(name = "costsSourceId") val costsSourceId: Int,
    @ColumnInfo(name = "costsSum") val costsSum : Float,
    @ColumnInfo(name = "costsDate") val costsDate : String

)