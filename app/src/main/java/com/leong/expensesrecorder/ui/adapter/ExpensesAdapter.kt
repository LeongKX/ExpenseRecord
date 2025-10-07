package com.leong.expensesrecorder.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.leong.expensesrecorder.data.models.Expense
import com.leong.expensesrecorder.databinding.LayoutExpenseBinding


class ExpensesAdapter(
    private var expenses: List<Expense>,
    private val onPress: (Expense, ActionType) -> Unit
):RecyclerView.Adapter<ExpensesAdapter.ExpenseViewHolder>() {

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

        holder.binding.run {
            tvItemName.text = expense.itemName
            tvPrice.text = expense.price.toString()
            tvCategory.text = expense.category.toString()
            tvQuantity.text = expense.quantity.toString()
            tvDateTime.text = expense.date.toString()

            mbUpdate.setOnClickListener {
                onPress(expense, ActionType.UPDATE)
            }

            mbDelete.setOnClickListener {
                onPress(expense, ActionType.DELETE)
            }

        }
    }

    override fun getItemCount() = expenses.size

    fun setExpenses(expenses: List<Expense>) {
        this.expenses = expenses
        notifyDataSetChanged()
    }

    class ExpenseViewHolder(
        val binding: LayoutExpenseBinding
    ): RecyclerView.ViewHolder(binding.root)
}
