package com.leong.expensesrecorder.ui.monthlyChart

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.leong.expensesrecorder.data.models.MonthYear
import com.leong.expensesrecorder.databinding.FragmentMonthlyChartBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MonthlyChartFragment : Fragment() {

    private lateinit var binding: FragmentMonthlyChartBinding
    private val viewModel: MonthlyChartViewModel by viewModels {
        MonthlyChartViewModel.Factory
    }

    private val monthYear: MonthYear by lazy { MonthYear.now() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMonthlyChartBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tvMonthTitle.text = monthYear.label
        viewModel.loadMonthData(monthYear)

        lifecycleScope.launch {
            viewModel.totals.collectLatest { totals ->
                binding.tvTotal.text = String.format("Total: RM%.2f", totals.total)
                setupPieChart(totals)
            }
        }

        binding.mbViewAll.setOnClickListener {
            val action = MonthlyChartFragmentDirections
                .actionMonthlyChartFragment3ToHomeFragment()
            findNavController().navigate(action)
        }
    }

    private fun setupPieChart(totals: CategoryTotals) {
        val entries = createPieEntries(totals)
        val data = createPieData(entries)
        configurePieChart(data)
    }

    private val sliceColors = mutableListOf<Int>()

    private fun createPieEntries(totals: CategoryTotals): List<PieEntry> {
        val entries = mutableListOf<PieEntry>()
        sliceColors.clear()
        fun addSlice(value: Double, label: String, colorRes: Int) {
            if (value > 0) {
                entries.add(PieEntry(value.toFloat(), label))
                sliceColors.add(androidx.core.content.ContextCompat.getColor(requireContext(), colorRes))
            }
        }
        addSlice(totals.entertainment, "Entertainment", com.leong.expensesrecorder.R.color.cat_entertainment)
        addSlice(totals.shops, "Shops", com.leong.expensesrecorder.R.color.cat_shops)
        addSlice(totals.food, "Food & Drink", com.leong.expensesrecorder.R.color.cat_food)
        addSlice(totals.others, "Others", com.leong.expensesrecorder.R.color.cat_others)
        return entries
    }

    private fun createPieData(entries: List<PieEntry>): PieData {
        val dataSet = PieDataSet(entries, "")
        dataSet.colors = sliceColors.toList()
        dataSet.sliceSpace = 3f
        dataSet.selectionShift = 5f

        return PieData(dataSet).apply {
            setValueTextSize(12f)
            setValueTextColor(Color.WHITE)
        }
    }

    private fun configurePieChart(data: PieData) = binding.pieChart.apply {
        val onSurface = com.google.android.material.color.MaterialColors.getColor(
            this, com.google.android.material.R.attr.colorOnSurface
        )
        this.data = data
        description.isEnabled = false
        setUsePercentValues(true)
        isDrawHoleEnabled = true
        setHoleColor(Color.TRANSPARENT)
        setEntryLabelColor(onSurface)
        legend.textColor = onSurface
        centerText = monthYear.month.displayName
        setCenterTextColor(onSurface)
        setCenterTextSize(14f)

        legend.orientation = Legend.LegendOrientation.HORIZONTAL
        legend.isWordWrapEnabled = true
        legend.horizontalAlignment = Legend.LegendHorizontalAlignment.CENTER
        legend.verticalAlignment = Legend.LegendVerticalAlignment.BOTTOM

        animateY(1000)
        invalidate()
    }

}
