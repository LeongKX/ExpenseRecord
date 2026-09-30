package com.leong.expensesrecorder.data.models

import com.leong.expensesrecorder.data.enums.Months
import java.util.Calendar
import java.util.Date

/**
 * A specific calendar month in a specific year (e.g. January 2026).
 *
 * Grouping expenses by [Months] alone merged every January across years together;
 * carrying the [year] as well keeps each month's records separate after the new year.
 */
data class MonthYear(
    val year: Int,
    val month: Months
) : Comparable<MonthYear> {

    /** Human-readable label, e.g. "January 2026". */
    val label: String get() = "${month.displayName} $year"

    /** Inclusive start-of-month timestamp in millis. */
    val startMillis: Long
        get() = calendarAtStart().timeInMillis

    /** Exclusive start-of-next-month timestamp in millis. */
    val endMillisExclusive: Long
        get() = calendarAtStart().apply { add(Calendar.MONTH, 1) }.timeInMillis

    private fun calendarAtStart(): Calendar = Calendar.getInstance().apply {
        clear()
        set(year, month.monthIndex, 1, 0, 0, 0)
    }

    override fun compareTo(other: MonthYear): Int =
        compareValuesBy(this, other, { it.year }, { it.month.monthIndex })

    companion object {
        fun fromDate(date: Date): MonthYear {
            val calendar = Calendar.getInstance().apply { time = date }
            return MonthYear(
                year = calendar.get(Calendar.YEAR),
                month = Months.fromDate(date)
            )
        }

        fun now(): MonthYear = fromDate(Date())
    }
}
