package com.leong.expensesrecorder.ui.detail

import android.app.Dialog
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.graphics.drawable.toDrawable
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.button.MaterialButton
import com.leong.expensesrecorder.R
import com.leong.expensesrecorder.data.models.Expense
import com.leong.expensesrecorder.databinding.FragmentExpenseDetailBinding
import kotlinx.coroutines.launch

class ExpenseDetailFragment : Fragment() {

    private val viewModel: ExpenseDetailViewModel by viewModels {
        ExpenseDetailViewModel.Companion.Factory
    }

    private lateinit var binding: FragmentExpenseDetailBinding

    private val args: ExpenseDetailFragmentArgs by navArgs()

    private lateinit var expense: Expense

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_expense_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launch {
            expense = viewModel.getExpense(args.expenseId)
            setData(expense)
            lifecycleScope.launch {
                viewModel.finish.collect {
                    getExpense()
                }
            }
        }
    }

    fun setData(expense: Expense?) {
        binding.run {
            mtDetails.setNavigationOnClickListener { findNavController().popBackStack() }
            tvItemName.text = expense?.itemName
            tvCategory.text = expense?.category.toString()
            tvQuantity.text = expense?.quantity.toString()
            tvPrice.text = expense?.price.toString()
        }
        setOnClickListeners()
    }

    fun setOnClickListeners() {
        binding.run {
            mbAdd.setOnClickListener {
                val action = ExpenseDetailFragmentDirections.actionExpenseDetailFragmentToAddExpenseFragment()
                findNavController().navigate(action)
            }
            mbUpdate.setOnClickListener {
                val action = ExpenseDetailFragmentDirections.actionExpenseDetailFragmentToEditExpenseFragment(args.expenseId)
                findNavController().navigate(action)
            }
            mbDelete.setOnClickListener {
                val dialog = createDeleteDialog(args.expenseId)
                dialog.show()
            }
        }
    }

    suspend fun getExpense() {
        val newExpense = viewModel.getExpense(args.expenseId)
        expense = newExpense
        setData(newExpense)
    }

    fun createDeleteDialog(expenseId: Int): Dialog {
        return Dialog(requireContext()).apply {
            setContentView(R.layout.confirmation_dialog)
            window?.setBackgroundDrawable(Color.TRANSPARENT.toDrawable())
            findViewById<MaterialButton>(R.id.mbCancel).setOnClickListener { dismiss() }
            findViewById<MaterialButton>(R.id.mbConfirm).setOnClickListener {
                viewModel.deleteExpense(expenseId)
                findNavController().popBackStack()
                dismiss()
            }
        }
    }

}