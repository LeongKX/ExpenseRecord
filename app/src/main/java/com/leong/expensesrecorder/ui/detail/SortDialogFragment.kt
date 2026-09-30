package com.leong.expensesrecorder.ui.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.leong.expensesrecorder.R
import com.leong.expensesrecorder.data.enums.Category
import com.leong.expensesrecorder.data.enums.SortBy
import com.leong.expensesrecorder.data.enums.SortOrder
import com.leong.expensesrecorder.databinding.SortDialogBinding

class SortDialogFragment(
    private val currentSort: SortBy,
    private val currentOrder: SortOrder,
    private val currentCategory: Category?,
    private val onSortClick: (SortBy, SortOrder, Category?) -> Unit
) : BottomSheetDialogFragment() {

    private var _binding: SortDialogBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = SortDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        restoreSelection()

        binding.mbReset.setOnClickListener {
            binding.rbDate.isChecked = true
            binding.rbAscending.isChecked = true
            binding.rbAll.isChecked = true
        }

        binding.mbDone.setOnClickListener {
            val sortBy = when (binding.rgSort.checkedRadioButtonId) {
                R.id.rbAmount -> SortBy.AMOUNT
                else -> SortBy.DATE
            }
            val sortOrder = when (binding.rgOrder.checkedRadioButtonId) {
                R.id.rbAscending -> SortOrder.ASCENDING
                else -> SortOrder.DESCENDING
            }
            val selectedCategory = when (binding.rgCategory.checkedRadioButtonId) {
                R.id.rbEntertainment -> Category.ENTERTAINMENT
                R.id.rbShops -> Category.SHOPS
                R.id.rbFnD -> Category.FOOD_AND_DRINK
                R.id.rbOthers -> Category.OTHERS
                else -> null
            }
            onSortClick(sortBy, sortOrder, selectedCategory)
            dismiss()
        }
    }

    private fun restoreSelection() {
        when (currentSort) {
            SortBy.DATE -> binding.rbDate.isChecked = true
            SortBy.AMOUNT -> binding.rbAmount.isChecked = true
        }
        when (currentOrder) {
            SortOrder.ASCENDING -> binding.rbAscending.isChecked = true
            SortOrder.DESCENDING -> binding.rbDescending.isChecked = true
        }
        when (currentCategory) {
            Category.ENTERTAINMENT -> binding.rbEntertainment.isChecked = true
            Category.SHOPS -> binding.rbShops.isChecked = true
            Category.FOOD_AND_DRINK -> binding.rbFnD.isChecked = true
            Category.OTHERS -> binding.rbOthers.isChecked = true
            null -> binding.rbAll.isChecked = true
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
