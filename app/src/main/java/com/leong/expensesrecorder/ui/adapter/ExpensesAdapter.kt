package com.leong.expensesrecorder.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.leong.expensesrecorder.R
import com.leong.expensesrecorder.data.enums.Category
import com.leong.expensesrecorder.data.models.Expense
import com.leong.expensesrecorder.databinding.LayoutExpenseBinding
import java.text.SimpleDateFormat
import java.util.Locale


class ExpensesAdapter(
    private var expenses: List<Expense>,
    private val onPress: (Expense, ActionType) -> Unit
):RecyclerView.Adapter<ExpensesAdapter.ExpenseViewHolder>() {

    private val dateFormat = SimpleDateFormat("d MMM yyyy", Locale.getDefault())

    enum class ActionType {
        UPDATE,DELETE
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ):ExpenseViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = LayoutExpenseBinding.inflate(inflater,parent, false)
        return ExpenseViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ExpenseViewHolder, position: Int) {
        val expense = expenses[position]
        val context = holder.binding.root.context

        holder.binding.run {
            tvItemName.text = expense.itemName
            tvPrice.text = String.format(Locale.getDefault(), "RM%,.2f", expense.price)
            tvCategory.text = categoryLabel(expense.category)
            tvQuantity.text = context.getString(R.string.qty_format, expense.quantity)
            tvDateTime.text = dateFormat.format(expense.date)
            vCategoryDot.backgroundTintList = ContextCompat.getColorStateList(
                context, categoryColor(expense.category)
            )

            mbUpdate.setOnClickListener { onPress(expense, ActionType.UPDATE) }
            mbDelete.setOnClickListener { onPress(expense, ActionType.DELETE) }
        }
    }

    override fun getItemCount() = expenses.size

    fun setExpenses(expenses: List<Expense>) {
        this.expenses = expenses
        notifyDataSetChanged()
    }

    private fun categoryLabel(category: Category): String = when (category) {
        Category.ENTERTAINMENT -> "Entertainment"
        Category.SHOPS -> "Shops"
        Category.FOOD_AND_DRINK -> "Food and Drink"
        Category.OTHERS -> "Others"
    }

    private fun categoryColor(category: Category): Int = when (category) {
        Category.ENTERTAINMENT -> R.color.cat_entertainment
        Category.SHOPS -> R.color.cat_shops
        Category.FOOD_AND_DRINK -> R.color.cat_food
        Category.OTHERS -> R.color.cat_others
    }

    class ExpenseViewHolder(
        val binding: LayoutExpenseBinding
    ): RecyclerView.ViewHolder(binding.root)
}
