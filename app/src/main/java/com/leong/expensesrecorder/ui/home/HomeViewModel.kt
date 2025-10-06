package com.leong.expensesrecorder.ui.home

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.leong.expensesrecorder.MyApp
import com.leong.expensesrecorder.data.enums.Months
import com.leong.expensesrecorder.data.repo.ExpensesRepo
import kotlinx.coroutines.launch

class HomeViewModel(
    repo: ExpensesRepo
): BaseHomeManageViewModel(repo){
    init {
        getMonths()
    }

    override fun getMonths() {
        viewModelScope.launch {
            Months.entries.forEach {
                it.toString()
            }
        }
    }


    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val myRepository = (this[APPLICATION_KEY] as MyApp).repo
                HomeViewModel(repo = myRepository)
            }
        }
    }
}