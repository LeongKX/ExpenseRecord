package com.leong.expensesrecorder.ui.monthlyChart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.leong.expensesrecorder.MyApp
import com.leong.expensesrecorder.data.enums.Category
import com.leong.expensesrecorder.data.models.Expense
import com.leong.expensesrecorder.data.models.MonthYear
import com.leong.expensesrecorder.data.repo.ExpensesRepo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class CategoryTotals(
    val entertainment: Double = 0.0,
    val shops: Double = 0.0,
    val food: Double = 0.0,
    val others: Double = 0.0,
    val total: Double = 0.0
)

class MonthlyChartViewModel(
    private val repo: ExpensesRepo
) : ViewModel() {

    private val _expenses = MutableStateFlow<List<Expense>>(emptyList())
    val expenses: StateFlow<List<Expense>> = _expenses

    private val _totals = MutableStateFlow(CategoryTotals())
    val totals: StateFlow<CategoryTotals> = _totals

    fun loadMonthData(monthYear: MonthYear) {
        viewModelScope.launch {
            val expensesList = repo.getExpensesByMonthYear(monthYear)
            _expenses.value = expensesList
            _totals.value = calculateTotals(expensesList)
        }
    }

    private fun calculateTotals(expenses: List<Expense>): CategoryTotals {
        var entertainment = 0.0
        var shops = 0.0
        var food = 0.0
        var others = 0.0

        for (e in expenses) {
            when (e.category) {
                Category.ENTERTAINMENT -> entertainment += e.price
                Category.SHOPS -> shops += e.price
                Category.FOOD_AND_DRINK -> food += e.price
                Category.OTHERS -> others += e.price
            }
        }

        val total = entertainment + shops + food + others
        return CategoryTotals(entertainment, shops, food, others, total)
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val myRepository = (this[APPLICATION_KEY] as MyApp).repo
                MonthlyChartViewModel(repo = myRepository)
            }
        }
    }
}
