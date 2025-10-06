package com.leong.expensesrecorder.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.leong.expensesrecorder.data.enums.Months
import com.leong.expensesrecorder.databinding.LayoutExpenseItemBinding

class MonthsAdapter(
    private val onPress: (Months) -> Unit
) : RecyclerView.Adapter<MonthsAdapter.MonthViewHolder>() {
    private val months = Months.entries.toList()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MonthViewHolder {
        val binding = LayoutExpenseItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return MonthViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: MonthViewHolder,
        position: Int
    ) {
        val month = months[position]
        holder.binding.run {
            tvMonth.text = month.name

            cvExpense.setOnClickListener {
                onPress(month)
//                true
            }
        }
    }

    override fun getItemCount():Int = months.size


    class MonthViewHolder(
        val binding: LayoutExpenseItemBinding
    ): RecyclerView.ViewHolder(binding.root){
//        fun bind(month: Months) {
//            binding.tvMonth.text = month.name
//        }
    }

}