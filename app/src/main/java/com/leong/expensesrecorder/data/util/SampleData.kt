package com.leong.expensesrecorder.data.util

import com.leong.expensesrecorder.data.enums.Category
import com.leong.expensesrecorder.data.models.Expense
import java.util.Calendar
import java.util.Date
import kotlin.random.Random

/**
 * Demo data used to seed an empty database so the app has something to show
 * across multiple years (year dropdown, charts, totals). Only inserted once,
 * when the database is empty — see [com.leong.expensesrecorder.MyApp].
 */
object SampleData {

    private data class Template(
        val name: String,
        val category: Category,
        val minPrice: Double,
        val maxPrice: Double,
        val maxQty: Int = 1
    )

    private val templates = listOf(
        // Food & Drink
        Template("Lunch at cafe", Category.FOOD_AND_DRINK, 8.0, 22.0),
        Template("Groceries", Category.FOOD_AND_DRINK, 30.0, 120.0),
        Template("Coffee", Category.FOOD_AND_DRINK, 6.0, 15.0),
        Template("Dinner with friends", Category.FOOD_AND_DRINK, 25.0, 80.0),
        Template("Bubble tea", Category.FOOD_AND_DRINK, 7.0, 14.0, maxQty = 3),
        // Shops
        Template("T-shirt", Category.SHOPS, 25.0, 70.0),
        Template("Sneakers", Category.SHOPS, 120.0, 350.0),
        Template("Phone charger", Category.SHOPS, 15.0, 45.0),
        Template("Household items", Category.SHOPS, 20.0, 90.0),
        Template("Books", Category.SHOPS, 18.0, 60.0, maxQty = 2),
        // Entertainment
        Template("Movie ticket", Category.ENTERTAINMENT, 12.0, 25.0, maxQty = 2),
        Template("Spotify subscription", Category.ENTERTAINMENT, 15.0, 20.0),
        Template("Concert", Category.ENTERTAINMENT, 80.0, 300.0),
        Template("Video game", Category.ENTERTAINMENT, 50.0, 220.0),
        // Others
        Template("Bus fare", Category.OTHERS, 2.0, 8.0),
        Template("Petrol", Category.OTHERS, 40.0, 120.0),
        Template("Phone bill", Category.OTHERS, 30.0, 80.0),
        Template("Gift", Category.OTHERS, 20.0, 150.0)
    )

    fun expenses(): List<Expense> {
        val random = Random(42) // fixed seed → consistent demo data
        val result = mutableListOf<Expense>()

        val now = Calendar.getInstance()
        val currentYear = now.get(Calendar.YEAR)
        val currentMonth = now.get(Calendar.MONTH)

        // Three years of history ending with the current year.
        val years = (currentYear - 2..currentYear).toList()

        for (year in years) {
            for (month in 0..11) {
                // Don't create expenses in the future for the current year.
                if (year == currentYear && month > currentMonth) continue

                val itemsThisMonth = random.nextInt(2, 6)
                repeat(itemsThisMonth) {
                    val template = templates[random.nextInt(templates.size)]
                    val price = round2(
                        template.minPrice + random.nextDouble() * (template.maxPrice - template.minPrice)
                    )
                    val quantity = if (template.maxQty > 1) random.nextInt(1, template.maxQty + 1) else 1
                    val day = random.nextInt(1, 28)

                    result += Expense(
                        itemName = template.name,
                        category = template.category,
                        quantity = quantity,
                        price = price,
                        date = dateOf(year, month, day)
                    )
                }
            }
        }
        return result
    }

    private fun dateOf(year: Int, month: Int, day: Int): Date {
        val calendar = Calendar.getInstance().apply {
            clear()
            set(year, month, day, 12, 0, 0)
        }
        return calendar.time
    }

    private fun round2(value: Double): Double = Math.round(value * 100.0) / 100.0
}
