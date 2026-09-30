package com.leong.expensesrecorder.ui.home

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.leong.expensesrecorder.MyApp
import com.leong.expensesrecorder.data.repo.ExpensesRepo

class HomeViewModel(
    repo: ExpensesRepo
): BaseHomeManageViewModel(repo){

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val myRepository = (this[APPLICATION_KEY] as MyApp).repo
                HomeViewModel(repo = myRepository)
            }
        }
    }
}
