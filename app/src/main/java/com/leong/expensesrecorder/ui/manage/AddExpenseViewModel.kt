package com.leong.expensesrecorder.ui.manage

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.leong.expensesrecorder.MyApp
import com.leong.expensesrecorder.data.models.Expense
import com.leong.expensesrecorder.data.repo.ExpensesRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AddExpenseViewModel(
    repo: ExpensesRepo
): BaseManageViewModel(repo) {

    override fun add(expense: Expense) {
        try {
            viewModelScope.launch(Dispatchers.IO) {
                require(expense.itemName.isNotBlank()) { "NO_TITLE" }
                require(expense.price.toString().isNotBlank()) { "NO_PRICE" }
                repo.addExpense(expense)
                _finish.emit(Unit)
            }
        } catch (e: Exception) {
            viewModelScope.launch { _error.emit(e.message.toString()) }
        }
    }


    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val myRepository = (this[APPLICATION_KEY] as MyApp).repo
                AddExpenseViewModel(repo = myRepository)
            }
        }
    }
}