package com.leong.expensesrecorder.ui.manage

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.chip.Chip
import com.google.android.material.snackbar.Snackbar
import com.leong.expensesrecorder.R
import com.leong.expensesrecorder.data.enums.Category
import com.leong.expensesrecorder.databinding.ManageExpenseLayoutBinding
import kotlinx.coroutines.launch

abstract class BaseManageFragment : Fragment() {

    protected lateinit var binding: ManageExpenseLayoutBinding
    protected abstract val viewModel: BaseManageViewModel

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
                    "NO_TITLE" -> "notitle"
                    "NO_MEANING" -> "nomeaning"
                    else -> "error"
                }
                showError(message)
            }
        }
        //The back button on the toolbar. Navigates back
        binding.mtManage.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
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
        snackbar.setBackgroundTint(ContextCompat.getColor(requireContext(), R.color.red)).show()
    }

}