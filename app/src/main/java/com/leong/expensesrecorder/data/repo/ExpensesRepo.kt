package com.leong.expensesrecorder.data.repo

import com.leong.expensesrecorder.data.enums.Months
import com.leong.expensesrecorder.data.models.Expense
import com.leong.expensesrecorder.database.ExpensesDao
import java.text.SimpleDateFormat
import java.util.Locale

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

    suspend fun getExpensesByMonth(month: Months): List<Expense> {
        val allExpenses = dao.getAllExpensesOnce()

        val formatter = SimpleDateFormat("MMMM", Locale.getDefault())

        return allExpenses.filter { expense ->
            val expenseMonth = formatter.format(expense.date).uppercase(Locale.getDefault())
            expenseMonth == month.name
        }
    }


}