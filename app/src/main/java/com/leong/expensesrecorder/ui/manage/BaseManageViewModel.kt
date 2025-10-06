package com.leong.expensesrecorder.ui.manage

import androidx.lifecycle.ViewModel
import com.leong.expensesrecorder.data.models.Expense
import com.leong.expensesrecorder.data.repo.ExpensesRepo
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow

abstract class BaseManageViewModel(
    protected val repo: ExpensesRepo
) : ViewModel() {
    protected val _finish = MutableSharedFlow<Unit>()

    val finish: SharedFlow<Unit> = _finish
    protected val _error = MutableSharedFlow<String>()

    val error: SharedFlow<String> = _error

    abstract fun add(expense: Expense)

}