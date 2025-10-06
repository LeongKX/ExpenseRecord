package com.leong.expensesrecorder.ui.home

import android.app.Dialog
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toDrawable
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.button.MaterialButton
import com.leong.expensesrecorder.R
import com.leong.expensesrecorder.databinding.FragmentHomeBinding
import com.leong.expensesrecorder.ui.adapter.ExpensesAdapter
import kotlinx.coroutines.launch
import kotlin.getValue

abstract class BaseHomeManageFragment : Fragment() {

    protected lateinit var binding: FragmentHomeBinding
    protected lateinit var adapter: ExpensesAdapter
    protected abstract val viewModel: BaseHomeManageViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupAdapter()

        lifecycleScope.launch {
            viewModel.expenses.collect {
                adapter.setExpenses(it)
//                binding.llEmpty.visibility = if (it.isEmpty()) View.VISIBLE else View.GONE
            }
        }
    }

    fun setupAdapter() {
        adapter = ExpensesAdapter(
            emptyList(),
            onLongPress = { expense ->
                val dialog = deleteAndUpdateDialog(expense.id!!)
                dialog.show()
            }
        )

        binding.rvExpenses.adapter = adapter
        binding.rvExpenses.layoutManager = LinearLayoutManager(this.context)
    }

    fun deleteAndUpdateDialog(expenseId:Int): Dialog {
        val dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.confirmation_dialog)
        dialog.window?.setBackgroundDrawable(Color.TRANSPARENT.toDrawable())
//        val tvConfirm = dialog.findViewById<TextView>(R.id.tvConfirm)
//        tvConfirm?.text = if (word.status == Status.INCOMPLETE) {
//            getString(R.string.to_complete_question)
//        } else {
//            getString(R.string.to_new_question)
//        }
        val mbDelete = dialog.findViewById<MaterialButton>(R.id.mbCancel)
        mbDelete?.text = "Delete"
        mbDelete?.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.red))
        mbDelete?.setOnClickListener {
            lifecycleScope.launch {
                viewModel.deleteExpense(expenseId)
                findNavController().popBackStack()
                dialog.dismiss()
            }
        }

        val mbUpdate = dialog.findViewById<MaterialButton>(R.id.mbConfirm)
        mbUpdate?.text = "Update"
        mbUpdate?.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.teal))
        mbUpdate?.setOnClickListener {
            lifecycleScope.launch {
//                setStatus()
                val action = HomeFragmentDirections.actionHomeFragmentToEditExpenseFragment(expenseId)
                findNavController().navigate(action)
                dialog.dismiss()
            }
        }
        return dialog

//    protected fun navigateToConfirmationDialog(expenseId: Int) {
//        val action = getWordDetailAction(expenseId)
//        findNavController().navigate(action)
    }

//    protected abstract fun getWordDetailAction(expenseId: Int): NavDirections

}
