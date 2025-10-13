package com.leong.expensesrecorder.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.leong.expensesrecorder.data.enums.Months
import com.leong.expensesrecorder.databinding.FragmentHomeBinding
import com.leong.expensesrecorder.ui.adapter.MonthsAdapter
import kotlinx.coroutines.launch

abstract class BaseHomeManageFragment : Fragment() {
    protected lateinit var binding: FragmentHomeBinding
    protected lateinit var adapter: MonthsAdapter
    protected abstract val viewModel: BaseHomeManageViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launch {
            val totals = viewModel.getMonthlyTotals()
            setupAdapter(totals)
        }

        binding.mtManage.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
    }

    fun setupAdapter(totals: Map<Months, Double>) {
        adapter = MonthsAdapter(
            monthTotals = totals,
            onPress = { month ->
                navigateToDetails(month)
            }
        )

        binding.rvExpenses.adapter = adapter
        binding.rvExpenses.layoutManager = LinearLayoutManager(this.context)
        }

    protected fun navigateToDetails(months: Months) {
        val action = getExpenseDetailAction(months)
        findNavController().navigate(action)
    }



    protected abstract fun getExpenseDetailAction(months: Months): NavDirections

}

