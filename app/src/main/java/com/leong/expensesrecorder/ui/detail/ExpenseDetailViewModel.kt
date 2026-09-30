package com.leong.expensesrecorder.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.leong.expensesrecorder.MyApp
import com.leong.expensesrecorder.data.enums.Category
import com.leong.expensesrecorder.data.enums.SortBy
import com.leong.expensesrecorder.data.enums.SortOrder
import com.leong.expensesrecorder.data.models.Expense
import com.leong.expensesrecorder.data.models.MonthYear
import com.leong.expensesrecorder.data.repo.ExpensesRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch

class ExpenseDetailViewModel(
    private val repo: ExpensesRepo
): ViewModel() {
    private val _finish = MutableSharedFlow<Unit>()
    val finish: SharedFlow<Unit> = _finish
    private var currentSort = SortBy.DATE
    private var currentOrder = SortOrder.ASCENDING
    private var currentSearch = ""
    private var currentCategoryFilter: Category? = null

    suspend fun getExpensesByMonth(monthYear: MonthYear): List<Expense> {
        val allExpenses = repo.getExpensesByMonthYear(monthYear)
        return allExpenses
            .filterCategory()
            .filterSearch()
            .applySort(currentSort, currentOrder)
    }


    fun deleteExpense(expenseId: Int?) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.deleteExpense(expenseId)
            _finish.emit(Unit)
        }
    }

    fun setSearch(str: String) {
        currentSearch = str
    }

    fun setSorting(sortBy: SortBy, sortOrder: SortOrder) {
        currentSort = sortBy
        currentOrder = sortOrder
    }

    fun setCategoryFilter(category: Category?) {
        currentCategoryFilter = category
    }

    /** The currently applied category filter, so the dialog can pre-select it. */
    fun currentCategory(): Category? = currentCategoryFilter


    private fun List<Expense>.applySort(sortBy: SortBy, sortOrder: SortOrder): List<Expense> {
        return when (sortBy) {
            SortBy.DATE -> when (sortOrder) {
                SortOrder.ASCENDING -> sortedBy { it.date }
                SortOrder.DESCENDING -> sortedByDescending { it.date }
            }

            SortBy.AMOUNT -> when (sortOrder) {
                SortOrder.ASCENDING -> sortedBy { it.price }
                SortOrder.DESCENDING -> sortedByDescending { it.price }
            }
        }
    }

    private fun List<Expense>.filterCategory(): List<Expense> {
        return currentCategoryFilter?.let { selectedCategory ->
            filter { it.category == selectedCategory }
        } ?: this
    }


    private fun List<Expense>.filterSearch(): List<Expense> {
        if (currentSearch.isBlank()) return this
        return this.filter {
            it.itemName.contains(currentSearch, ignoreCase = true)
        }
    }
    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val myRepository =
                    (this[ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY] as MyApp).repo
                ExpenseDetailViewModel(repo = myRepository)
            }
        }
    }
}