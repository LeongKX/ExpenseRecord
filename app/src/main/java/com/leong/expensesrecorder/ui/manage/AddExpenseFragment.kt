package com.leong.expensesrecorder.ui.manage

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.google.android.material.chip.Chip
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
            mbSubmit.text = "Add"
            mbSubmit.setOnClickListener {
                val checkedId = cgCategory.checkedChipId
                val selectedChip = cgCategory.findViewById<Chip>(checkedId)
                val selectedCategory = selectedChip?.tag as? Category?: Category.OTHERS
                viewModel.add(
                    Expense(
                        etItemName.text.toString(),
                        category = selectedCategory,
                        etQuantity.text.toString().toInt(),
                        etPrice.text.toString().toDouble()
                    )
                )
            }
        }
//        lifecycleScope.launch {
//            viewModel.finish.collect {
//                findNavController().popBackStack()
//            }
//        }
    }
}