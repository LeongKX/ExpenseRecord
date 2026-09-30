package com.leong.expensesrecorder

import android.app.Application
import androidx.room.Room
import com.leong.expensesrecorder.data.repo.ExpensesRepo
import com.leong.expensesrecorder.data.util.SampleData
import com.leong.expensesrecorder.database.MyDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class MyApp: Application() {

    lateinit var repo: ExpensesRepo

    private val appScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    override fun onCreate() {
        super.onCreate()

        val db = Room.databaseBuilder(
            this,
            MyDatabase::class.java,
            MyDatabase.NAME
        )
            .build()
        repo = ExpensesRepo(db.getExpensesDao())

        seedSampleDataIfEmpty()
    }

    /** Populate demo data the first time the app runs (empty database only). */
    private fun seedSampleDataIfEmpty() {
        appScope.launch {
            if (repo.getAllExpensesOnce().isEmpty()) {
                SampleData.expenses().forEach { repo.addExpense(it) }
            }
        }
    }
}
