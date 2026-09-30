package com.leong.expensesrecorder.ui.manage

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.chip.Chip
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.snackbar.Snackbar
import com.leong.expensesrecorder.R
import com.leong.expensesrecorder.data.enums.Category
import com.leong.expensesrecorder.databinding.ManageExpenseLayoutBinding
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone

abstract class BaseManageFragment : Fragment() {

    protected lateinit var binding: ManageExpenseLayoutBinding
    protected abstract val viewModel: BaseManageViewModel

    /** Date chosen for the expense; defaults to today and can be any date (past or future). */
    protected var selectedDate: Date = Date()

    private val dateLabelFormat = SimpleDateFormat("d MMM yyyy", Locale.getDefault())

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = ManageExpenseLayoutBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //If it receives a SharedFlow emit of finish, it will popBackStack()
        lifecycleScope.launch {
            viewModel.finish.collect {
                findNavController().popBackStack()
            }
        }
        //If it receives a SharedFlow emit of error, it will show a snackbar
        lifecycleScope.launch {
            viewModel.error.collect {
                val message = when (it) {
                    "NO_TITLE" -> "Please enter item name"
                    "NO_QUANTITY" -> "Please enter quantity"
                    "NO_PRICE" -> "Please enter price"
                    "NO_CATEGORY" -> "Please select a category"
                    else -> "Unknown error occurred"
                }
                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
            }
        }

        //The back button on the toolbar. Navigates back
        binding.mtManage.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
    }

    /** Wire the read-only date field to a Material date picker. Call from onViewCreated. */
    protected fun setupDatePicker() {
        updateDateField()
        binding.etDate.setOnClickListener { showDatePicker() }
        binding.tilDate.setEndIconOnClickListener { showDatePicker() }
    }

    /** Reflect [selectedDate] in the text field. Call after changing it (e.g. when editing). */
    protected fun updateDateField() {
        binding.etDate.setText(dateLabelFormat.format(selectedDate))
    }

    private fun showDatePicker() {
        val picker = MaterialDatePicker.Builder.datePicker()
            .setTitleText(R.string.select_date)
            .setSelection(selectedDate.toUtcMidnight())
            .build()

        picker.addOnPositiveButtonClickListener { utcMillis ->
            selectedDate = utcMillisToLocalDate(utcMillis)
            updateDateField()
        }
        picker.show(childFragmentManager, "date_picker")
    }

    // MaterialDatePicker works in UTC midnight; convert to/from a local calendar date
    // so the stored day never shifts across time zones.
    private fun Date.toUtcMidnight(): Long {
        val local = Calendar.getInstance().apply { time = this@toUtcMidnight }
        return Calendar.getInstance(TimeZone.getTimeZone("UTC")).apply {
            clear()
            set(
                local.get(Calendar.YEAR),
                local.get(Calendar.MONTH),
                local.get(Calendar.DAY_OF_MONTH)
            )
        }.timeInMillis
    }

    private fun utcMillisToLocalDate(utcMillis: Long): Date {
        val utc = Calendar.getInstance(TimeZone.getTimeZone("UTC")).apply { timeInMillis = utcMillis }
        return Calendar.getInstance().apply {
            clear()
            set(
                utc.get(Calendar.YEAR),
                utc.get(Calendar.MONTH),
                utc.get(Calendar.DAY_OF_MONTH),
                12, 0, 0
            )
        }.time
    }

    protected fun setupExpenseTypes() {
        binding.cgCategory.removeAllViews()
        Category.entries.forEach { category ->
            val chip = Chip(requireContext()).apply {
                text = category.name
                isCheckable = true
                tag = category
                isChecked = (category == Category.OTHERS)
            }
            binding.cgCategory.addView(chip)
        }
    }

    fun showError(msg: String) {
        val snackbar = Snackbar.make(binding.root, msg, Snackbar.LENGTH_LONG)
        snackbar.setBackgroundTint(
            ContextCompat.getColor(requireContext(), R.color.red)
        ).show()
    }

}