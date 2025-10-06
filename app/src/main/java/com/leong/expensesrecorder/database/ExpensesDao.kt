package com.leong.expensesrecorder.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.leong.expensesrecorder.data.models.Expense
import kotlinx.coroutines.flow.Flow

@Dao
interface ExpensesDao {

    @Query("SELECT * FROM expense")
    fun getAllExpenses(): Flow<List<Expense>>

    @Query("SELECT * FROM expense WHERE id = :id")
    suspend fun getExpenseById(id: Int): Expense?

    @Insert
    fun addExpense(expense: Expense)

    @Update
    fun update(expense: Expense)

    @Query("DELETE FROM expense WHERE id = :id")
    fun delete(id: Int)

    @Query("SELECT * FROM expense WHERE month = :month")
    suspend fun getExpensesByMonth(month: String): List<Expense>


}