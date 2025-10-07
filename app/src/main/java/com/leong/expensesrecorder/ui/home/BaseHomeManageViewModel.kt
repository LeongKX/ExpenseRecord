package com.leong.expensesrecorder.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.leong.expensesrecorder.data.enums.Months
import com.leong.expensesrecorder.data.models.Expense
import com.leong.expensesrecorder.data.repo.ExpensesRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

abstract class BaseHomeManageViewModel(
    protected val repo: ExpensesRepo
): ViewModel() {
    protected val _expenses = MutableStateFlow<List<Expense>>(emptyList())

    val expenses: StateFlow<List<Expense>> = _expenses

    abstract fun getMonths()

    suspend fun getMonthlyTotals(): Map<Months, Double> {
        val allExpenses = repo.getAllExpensesOnce()
        return allExpenses
            .groupBy { expense -> Months.fromDate(expense.date) }
            .mapValues { (_, expenses) ->
                expenses.sumOf { it.price }
            }
    }

}