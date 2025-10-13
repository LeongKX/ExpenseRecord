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
) : BaseManageViewModel(repo) {

    override fun add(expense: Expense) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                // ✅ Validate inside the same coroutine scope
                require(expense.itemName.isNotBlank()) { "NO_TITLE" }
                require(expense.quantity > 0) { "NO_QUANTITY" }
                require(expense.price > 0) { "NO_PRICE" }
                require(expense.category != null) { "NO_CATEGORY" }

                repo.addExpense(expense)
                _finish.emit(Unit)
            } catch (e: IllegalArgumentException) {
                // validation error
                _error.emit(e.message ?: "UNKNOWN_ERROR")
            } catch (e: Exception) {
                // unexpected error
                _error.emit("Something went wrong")
            }
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
