package com.leong.expensesrecorder.data.repo

import com.leong.expensesrecorder.data.models.Expense
import com.leong.expensesrecorder.data.models.MonthYear
import com.leong.expensesrecorder.database.ExpensesDao

class ExpensesRepo(
    private val dao: ExpensesDao
) {
    fun addExpense(expense: Expense) {
        dao.addExpense(expense)
    }

    suspend fun getAllExpensesOnce(): List<Expense> {
        return dao.getAllExpensesOnce()
    }


    suspend fun getExpenseById(id: Int): Expense? {
        return dao.getExpenseById(id)
    }

    fun editExpense(expense: Expense) {
        dao.update(expense)
    }

    fun deleteExpense(id: Int?) {
        dao.delete(id)
    }

    suspend fun getExpensesByMonthYear(monthYear: MonthYear): List<Expense> {
        return dao.getExpensesBetween(monthYear.startMillis, monthYear.endMillisExclusive)
    }


}