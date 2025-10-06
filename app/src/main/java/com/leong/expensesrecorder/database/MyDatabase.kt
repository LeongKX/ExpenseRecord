package com.leong.expensesrecorder.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.leong.expensesrecorder.data.models.Expense

@Database(entities = [Expense::class], version = 1)
//@TypeConverters(Converters::class)
abstract class MyDatabase: RoomDatabase() {
    abstract fun getExpensesDao(): ExpensesDao

    companion object {
        const val NAME = "my_database"
    }
}