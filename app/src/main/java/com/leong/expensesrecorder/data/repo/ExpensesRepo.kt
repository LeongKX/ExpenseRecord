package com.leong.expensesrecorder.data.repo

import com.leong.expensesrecorder.data.models.Expense
import com.leong.expensesrecorder.database.ExpensesDao
import kotlinx.coroutines.flow.Flow

class ExpensesRepo(
    private val dao: ExpensesDao
) {
    fun addExpense(expense: Expense) {
        dao.addExpense(expense)
    }

    fun getAllExpenses(): Flow<List<Expense>> {
        return dao.getAllExpenses()
    }

    suspend fun getExpenseById(id: Int): Expense? {
        return dao.getExpenseById(id)
    }

    fun editExpense(expense: Expense) {
        dao.update(expense)
    }

    fun deleteExpense(id: Int) {
        dao.delete(id)
    }
}