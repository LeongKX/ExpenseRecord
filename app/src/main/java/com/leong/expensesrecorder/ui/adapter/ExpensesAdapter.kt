package com.leong.expensesrecorder.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.leong.expensesrecorder.data.models.Expense
import com.leong.expensesrecorder.databinding.LayoutExpenseItemBinding

class ExpensesAdapter(
    private var expenses: List<Expense>,
//    private val onLongPress: (Expense) -> Unit
):RecyclerView.Adapter<ExpensesAdapter.ExpenseViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ):ExpenseViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = LayoutExpenseItemBinding.inflate(inflater,parent, false)
        return ExpenseViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ExpenseViewHolder, position: Int) {
        val expense = expenses[position]

        holder.binding.run {
            tvMonth.text = expense.category.toString()
            tvPrice.text = expense.price.toString()

//            cvExpense.setOnLongClickListener {
//                onLongPress(expense)
//                true
//            }
        }
    }

    override fun getItemCount() = expenses.size

    fun setExpenses(expenses: List<Expense>) {
        this.expenses = expenses
        notifyDataSetChanged()
    }

    class ExpenseViewHolder(
        val binding: LayoutExpenseItemBinding
    ): RecyclerView.ViewHolder(binding.root)
}
