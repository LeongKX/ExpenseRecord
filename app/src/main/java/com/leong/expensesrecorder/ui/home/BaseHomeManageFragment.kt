package com.leong.expensesrecorder.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.leong.expensesrecorder.data.models.MonthYear
import com.leong.expensesrecorder.databinding.FragmentHomeBinding
import com.leong.expensesrecorder.ui.adapter.MonthsAdapter
import kotlinx.coroutines.launch
import java.util.Locale

abstract class BaseHomeManageFragment : Fragment() {
    protected lateinit var binding: FragmentHomeBinding
    protected lateinit var adapter: MonthsAdapter
    protected abstract val viewModel: BaseHomeManageViewModel

    private var selectedYear: Int = MonthYear.now().year

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = MonthsAdapter(emptyList()) { monthYear -> navigateToDetails(monthYear) }
        binding.rvExpenses.adapter = adapter
        binding.rvExpenses.layoutManager = LinearLayoutManager(requireContext())

        binding.mtManage.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        setupYearDropdown()
    }

    private fun setupYearDropdown() {
        lifecycleScope.launch {
            val years = viewModel.getAvailableYears()
            if (selectedYear !in years) selectedYear = years.first()

            val yearAdapter = ArrayAdapter(
                requireContext(),
                android.R.layout.simple_list_item_1,
                years.map { it.toString() }
            )
            binding.actYear.setAdapter(yearAdapter)
            binding.actYear.setText(selectedYear.toString(), false)
            binding.actYear.setOnItemClickListener { _, _, position, _ ->
                selectedYear = years[position]
                loadYear(selectedYear)
            }

            loadYear(selectedYear)
        }
    }

    private fun loadYear(year: Int) {
        lifecycleScope.launch {
            val totals = viewModel.getMonthlyTotals(year)
            adapter.updateTotals(totals.toList())

            val yearTotal = totals.values.sum()
            binding.tvYearTotalLabel.text = getString(
                com.leong.expensesrecorder.R.string.spent_in_year, year.toString()
            )
            binding.tvYearTotal.text = String.format(Locale.getDefault(), "RM%,.2f", yearTotal)

            val hasData = yearTotal > 0.0
            binding.llEmpty.visibility = if (hasData) View.GONE else View.VISIBLE
            binding.rvExpenses.visibility = if (hasData) View.VISIBLE else View.GONE
            binding.tvEmpty.text = getString(
                com.leong.expensesrecorder.R.string.no_expenses_year, year.toString()
            )
        }
    }

    protected fun navigateToDetails(monthYear: MonthYear) {
        val action = getExpenseDetailAction(monthYear)
        findNavController().navigate(action)
    }

    protected abstract fun getExpenseDetailAction(monthYear: MonthYear): NavDirections
}
