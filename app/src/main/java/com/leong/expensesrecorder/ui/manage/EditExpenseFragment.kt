package com.leong.expensesrecorder.ui.manage

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.google.android.material.chip.Chip
import com.leong.expensesrecorder.R
import com.leong.expensesrecorder.data.enums.Category
import com.leong.expensesrecorder.data.models.Expense
import kotlinx.coroutines.launch

class EditExpenseFragment : BaseManageFragment() {

    override val viewModel: EditExpenseViewModel by viewModels {
        EditExpenseViewModel.Factory
    }

    private lateinit var expense: Expense

    private val args: EditExpenseFragmentArgs by navArgs()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            chipEntertainment.tag = Category.ENTERTAINMENT
            chipShops.tag = Category.SHOPS
            chipFoodAndDrink.tag = Category.FOOD_AND_DRINK
            chipOthers.tag = Category.OTHERS
        }
        val expenseId = args.expenseId
        lifecycleScope.launch {
            expense = viewModel.getExpense(expenseId)
            selectedDate = expense.date
            setupDatePicker()
            binding.run {
                setExpense(expense)
                mbSubmit.setOnClickListener {
                    viewModel.add(
                        expense.copy(
                            itemName = etItemName.text.toString(),
                            category = binding.cgCategory.findViewById<Chip>(
                                binding.cgCategory.checkedChipId
                            ).tag as Category,
                            quantity = etQuantity.text.toString().toInt(),
                            price = etPrice.text.toString().toDouble(),
                            date = selectedDate
                        )
                    )
                }
            }
        }
    }

    fun setExpense(expense: Expense?) {
        binding.run {
            mbSubmit.text = getString(R.string.update)
            mtManage.title = getString(R.string.update_record)
            etItemName.setText(expense?.itemName)
            // Category Chip
            for (i in 0 until binding.cgCategory.childCount) {
                val chip = binding.cgCategory.getChildAt(i) as com.google.android.material.chip.Chip
                chip.isChecked = (chip.tag == expense?.category)
            }
            etQuantity.setText(expense?.quantity.toString())
            etPrice.setText(expense?.price.toString())
        }
    }


}