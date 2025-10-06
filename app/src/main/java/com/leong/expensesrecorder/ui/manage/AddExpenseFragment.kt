package com.leong.expensesrecorder.ui.manage

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.chip.Chip
import com.leong.expensesrecorder.R
import com.leong.expensesrecorder.data.enums.Category
import com.leong.expensesrecorder.data.models.Expense
import com.leong.expensesrecorder.ui.home.BaseHomeManageFragment
import kotlinx.coroutines.launch
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