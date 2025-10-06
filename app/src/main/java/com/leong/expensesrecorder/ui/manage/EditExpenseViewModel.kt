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

class EditExpenseViewModel(
    repo: ExpensesRepo
): BaseManageViewModel(repo) {
    private var expense: Expense? = null

    suspend fun getExpense(id: Int): Expense {
        val result = repo.getExpenseById(id) ?: throw Exception("Expense doesn't exist")
        expense = result
        return result
    }

    override fun add(newExpense: Expense) {
        try {
            viewModelScope.launch(Dispatchers.IO) {
                require(newExpense.itemName.isNotBlank()) {"No Item Name"}
                require(newExpense.price.toString().isNotBlank()) {"No Price"}
                expense?.let {
                    repo.editExpense(
                        it.copy(newExpense.itemName,newExpense.category,newExpense.quantity,newExpense.price)
                    )
                }
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
                EditExpenseViewModel(repo = myRepository)
            }
        }
    }
}