package com.leong.expensesrecorder.ui.manage

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.google.android.material.chip.Chip
import com.leong.expensesrecorder.R
import com.leong.expensesrecorder.data.enums.Category
import com.leong.expensesrecorder.data.models.Expense
import kotlin.getValue

class AddExpenseFragment : BaseManageFragment() {

    override val viewModel: AddExpenseViewModel by viewModels {
        AddExpenseViewModel.Factory
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupExpenseTypes()

        binding.run {
            mbSubmit.text = getString(R.string.add)
            mbSubmit.setOnClickListener {
                val itemName = etItemName.text.toString().trim()
                val quantityText = etQuantity.text.toString().trim()
                val priceText = etPrice.text.toString().trim()
                val checkedId = cgCategory.checkedChipId
                val selectedChip = cgCategory.findViewById<Chip>(checkedId)
                val selectedCategory = selectedChip?.tag as? Category

                val quantity = quantityText.toIntOrNull() ?: 0
                val price = priceText.toDoubleOrNull() ?: 0.0

                viewModel.add(
                    Expense(
                        itemName = itemName,
                        category = selectedCategory ?: Category.OTHERS,
                        quantity = quantity,
                        price = price
                    )
                )
            }
        }
    }
}
