package com.leong.expensesrecorder

import android.app.Application
import androidx.room.Room
import com.leong.expensesrecorder.data.repo.ExpensesRepo
import com.leong.expensesrecorder.database.MyDatabase

class MyApp: Application() {

    lateinit var repo: ExpensesRepo
    override fun onCreate() {
        super.onCreate()

        val db = Room.databaseBuilder(
            this,
            MyDatabase::class.java,
            MyDatabase.NAME
        )
            .build()
        repo = ExpensesRepo(db.getExpensesDao())
    }
}