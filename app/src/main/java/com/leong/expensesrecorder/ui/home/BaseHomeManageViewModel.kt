package com.leong.expensesrecorder.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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

    abstract fun getExpenses()

    fun deleteExpense(expenseId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.deleteExpense(expenseId)
        }
    }
}