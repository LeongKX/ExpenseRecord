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
import com.github.mikephil.charting.utils.ColorTemplate
import com.leong.expensesrecorder.data.enums.Months
import com.leong.expensesrecorder.databinding.FragmentMonthlyChartBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MonthlyChartFragment : Fragment() {

    private lateinit var binding: FragmentMonthlyChartBinding
    private val viewModel: MonthlyChartViewModel by viewModels {
        MonthlyChartViewModel.Factory
    }

    private val month: Months by lazy {
        Months.entries[java.time.LocalDate.now().monthValue - 1]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMonthlyChartBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tvMonthTitle.text = month.name
        viewModel.loadMonthData(month)

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

    private fun createPieEntries(totals: CategoryTotals): List<PieEntry> {
        val entries = mutableListOf<PieEntry>()
        if (totals.entertainment > 0) entries.add(PieEntry(totals.entertainment.toFloat(), "Entertainment"))
        if (totals.shops > 0) entries.add(PieEntry(totals.shops.toFloat(), "Shops"))
        if (totals.food > 0) entries.add(PieEntry(totals.food.toFloat(), "Food & Drink"))
        if (totals.others > 0) entries.add(PieEntry(totals.others.toFloat(), "Others"))
        return entries
    }

    private fun createPieData(entries: List<PieEntry>): PieData {
        val dataSet = PieDataSet(entries, "")
        dataSet.colors = ColorTemplate.MATERIAL_COLORS.toList()
        dataSet.sliceSpace = 3f
        dataSet.selectionShift = 5f

        return PieData(dataSet).apply {
            setValueTextSize(12f)
            setValueTextColor(Color.WHITE)
        }
    }

    private fun configurePieChart(data: PieData) = binding.pieChart.apply {
        this.data = data
        description.isEnabled = false
        setUsePercentValues(true)
        isDrawHoleEnabled = true
        setHoleColor(Color.TRANSPARENT)
        setEntryLabelColor(Color.BLACK)
        centerText = month.name
        setCenterTextSize(14f)

        legend.orientation = Legend.LegendOrientation.HORIZONTAL
        legend.isWordWrapEnabled = true
        legend.horizontalAlignment = Legend.LegendHorizontalAlignment.CENTER
        legend.verticalAlignment = Legend.LegendVerticalAlignment.BOTTOM

        animateY(1000)
        invalidate()
    }

}
