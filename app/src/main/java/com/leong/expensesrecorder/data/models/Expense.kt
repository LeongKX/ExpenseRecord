package com.leong.expensesrecorder.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.leong.expensesrecorder.data.enums.Category
@Entity
data class Expense(
//    val date: String,
    val itemName: String,
    val category: Category = Category.OTHERS,
    val quantity: Int = 1,
    val price: Double,
    @PrimaryKey val id: Int? = null
)