package com.leong.expensesrecorder.ui.detail

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.leong.expensesrecorder.R
import com.leong.expensesrecorder.data.enums.Category
import com.leong.expensesrecorder.data.enums.SortBy
import com.leong.expensesrecorder.data.enums.SortOrder
import com.leong.expensesrecorder.databinding.SortDialogBinding

class SortDialogFragment(
    private val currentSort: SortBy,
    private val currentOrder: SortOrder,
    private val onSortClick: (SortBy, SortOrder, Category?) -> Unit
) : DialogFragment() {

    private lateinit var binding: SortDialogBinding


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = SortDialogBinding.inflate(layoutInflater)
        setRadio(currentSort,currentOrder)
        val dialog = Dialog(requireContext())
        dialog.setContentView(binding.root)
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
            //Sends the information back to lambda
            onSortClick(sortBy, sortOrder, selectedCategory)
            dismiss()
        }
        return dialog
    }

    fun setRadio(currentSort: SortBy, currentOrder: SortOrder) {
        when (currentSort) {
            SortBy.DATE -> binding.rbDate.isChecked = true
            SortBy.AMOUNT -> binding.rbAmount.isChecked = true
        }
        when (currentOrder) {
            SortOrder.ASCENDING -> binding.rbAscending.isChecked = true
            SortOrder.DESCENDING -> binding.rbDescending.isChecked = true
        }
    }

}