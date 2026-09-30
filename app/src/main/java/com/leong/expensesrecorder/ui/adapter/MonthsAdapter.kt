package com.leong.expensesrecorder.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.leong.expensesrecorder.data.models.MonthYear
import com.leong.expensesrecorder.databinding.LayoutMonthBinding
import java.util.Locale

class MonthsAdapter(
    private var items: List<Pair<MonthYear, Double>>,
    private val onPress: (MonthYear) -> Unit
) : RecyclerView.Adapter<MonthsAdapter.MonthViewHolder>() {

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
        val (monthYear, total) = items[position]
        val hasSpending = total > 0.0
        holder.binding.apply {
            tvMonthBadge.text = monthYear.month.displayName.take(3)
            tvMonth.text = monthYear.label
            tvPrice.text = String.format(Locale.getDefault(), "RM%.2f", total)
            // Dim months with no spending so the ones that matter stand out.
            root.alpha = if (hasSpending) 1f else 0.55f
            cvExpense.setOnClickListener { onPress(monthYear) }
        }
    }

    override fun getItemCount(): Int = items.size

    fun updateTotals(newTotals: List<Pair<MonthYear, Double>>) {
        items = newTotals
        notifyDataSetChanged()
    }

    class MonthViewHolder(
        val binding: LayoutMonthBinding
    ): RecyclerView.ViewHolder(binding.root)
}
