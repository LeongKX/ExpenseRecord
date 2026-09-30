package com.leong.expensesrecorder.ui.home

import androidx.lifecycle.ViewModel
import com.leong.expensesrecorder.data.enums.Months
import com.leong.expensesrecorder.data.models.MonthYear
import com.leong.expensesrecorder.data.repo.ExpensesRepo

abstract class BaseHomeManageViewModel(
    protected val repo: ExpensesRepo
): ViewModel() {

    /** Years that have expenses, plus the current year, newest first. */
    suspend fun getAvailableYears(): List<Int> {
        val years = repo.getAllExpensesOnce()
            .map { MonthYear.fromDate(it.date).year }
            .toMutableSet()
        years += MonthYear.now().year
        return years.sortedDescending()
    }

    /** All 12 months of [year] with their totals (RM0.00 when empty), newest month first. */
    suspend fun getMonthlyTotals(year: Int): Map<MonthYear, Double> {
        val actualTotals = repo.getAllExpensesOnce()
            .groupBy { expense -> MonthYear.fromDate(expense.date) }
            .mapValues { (_, expenses) -> expenses.sumOf { it.price } }

        return Months.entries
            .associate { month ->
                val monthYear = MonthYear(year, month)
                monthYear to (actualTotals[monthYear] ?: 0.0)
            }
            .toSortedMap(compareByDescending { it })
    }

}
