package com.leong.expensesrecorder.data.enums

import java.util.Calendar
import java.util.Date

//enum class Months {
//    JANUARY,
//    FEBRUARY,
//    MARCH,
//    APRIL,
//    MAY,
//    JUNE,
//    JULY,
//    AUGUST,
//    SEPTEMBER,
//    OCTOBER,
//    NOVEMBER,
//    DECEMBER
//}

enum class Months(val monthIndex: Int) {
    JANUARY(0), FEBRUARY(1), MARCH(2), APRIL(3),
    MAY(4), JUNE(5), JULY(6), AUGUST(7),
    SEPTEMBER(8), OCTOBER(9), NOVEMBER(10), DECEMBER(11);

    /** Title-cased name for display, e.g. "January". */
    val displayName: String
        get() = name.lowercase().replaceFirstChar { it.uppercase() }

    companion object {
        fun fromDate(date: Date): Months {
            val calendar = Calendar.getInstance()
            calendar.time = date
            val monthIndex = calendar.get(Calendar.MONTH)
            return entries.first { it.monthIndex == monthIndex }
        }
    }
}
