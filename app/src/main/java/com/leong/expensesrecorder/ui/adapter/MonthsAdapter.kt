package com.leong.expensesrecorder.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.leong.expensesrecorder.data.enums.Months
import com.leong.expensesrecorder.databinding.LayoutMonthBinding

class MonthsAdapter(
    private var monthTotals: Map<Months, Double>,
    private val onPress: (Months) -> Unit
) : RecyclerView.Adapter<MonthsAdapter.MonthViewHolder>() {
    private val months = Months.entries.toList()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MonthViewHolder {
        val binding = LayoutMonthBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return MonthViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MonthViewHolder, position: Int) {
        val month = months[position]
        val total = monthTotals[month] ?: 0.0
        holder.binding.apply {
            tvMonth.text = month.name
            tvPrice.text = String.format("RM%.2f", total)
            cvExpense.setOnClickListener { onPress(month) }
        }
    }

    override fun getItemCount():Int = months.size

    fun updateTotals(newTotals: Map<Months, Double>) {
        monthTotals = newTotals
        notifyDataSetChanged()
    }
    class MonthViewHolder(
        val binding: LayoutMonthBinding
    ): RecyclerView.ViewHolder(binding.root)
}