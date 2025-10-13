package com.leong.expensesrecorder.ui.detail

import android.app.Dialog
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.graphics.drawable.toDrawable
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.button.MaterialButton
import com.leong.expensesrecorder.R
import com.leong.expensesrecorder.data.enums.Category
import com.leong.expensesrecorder.data.enums.SortBy
import com.leong.expensesrecorder.data.enums.SortOrder
import com.leong.expensesrecorder.data.models.Expense
import com.leong.expensesrecorder.data.util.Constant
import com.leong.expensesrecorder.databinding.FragmentExpenseDetailBinding
import com.leong.expensesrecorder.ui.adapter.ExpensesAdapter
import kotlinx.coroutines.launch
import com.leong.expensesrecorder.data.enums.Months


import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.utils.ColorTemplate


class ExpenseDetailFragment : Fragment() {

    private val viewModel: ExpenseDetailViewModel by viewModels {
        ExpenseDetailViewModel.Companion.Factory
    }

    private lateinit var binding: FragmentExpenseDetailBinding

    private val args: ExpenseDetailFragmentArgs by navArgs()

    private var expense: Expense? = null


    private lateinit var adapter: ExpensesAdapter

    private var currentSort = SortBy.DATE
    private var currentOrder = SortOrder.ASCENDING

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentExpenseDetailBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setTextListener()
        setNavigation()
        setupToolbar()
        setupAddButton()
        setupAdapter()
        observeExpenses()
    }

    private fun setupToolbar() {
        binding.mtDetails.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun setupAddButton() {
        val currentMonth = Months.entries[java.time.LocalDate.now().monthValue - 1]
        if (args.month == currentMonth) {
            binding.mbAdd.apply {
                visibility = View.VISIBLE
                setOnClickListener {
                    val action = ExpenseDetailFragmentDirections
                        .actionExpenseDetailFragmentToAddExpenseFragment()
                    findNavController().navigate(action)
                }
            }
        } else {
            binding.mbAdd.visibility = View.GONE
        }
    }

    private fun setupAdapter() {
        adapter = ExpensesAdapter(emptyList()) { expense, action ->
            when (action) {
                ExpensesAdapter.ActionType.UPDATE -> navigateToEdit(expense.id!!)
                ExpensesAdapter.ActionType.DELETE -> createDeleteDialog(expense.id).show()
            }
        }
    }

    private fun navigateToEdit(expenseId: Int) {
        val action = ExpenseDetailFragmentDirections
            .actionExpenseDetailFragmentToEditExpenseFragment(expenseId)
        findNavController().navigate(action)
    }

    private fun observeExpenses() {
        lifecycleScope.launch {
            val expenses = viewModel.getExpensesByMonth(args.month)
            if (expenses.isNotEmpty()) {
                adapter.setExpenses(expenses)
                setupRecyclerView()
                calculateCategoryTotals(expenses)
            } else {
                showNoData()
            }

            viewModel.finish.collect {
                getExpense()
            }
        }
    }



    private fun showNoData() {
        binding.run {
            mtDetails.setNavigationOnClickListener { findNavController().popBackStack() }
            setupRecyclerView()

            val currentMonth = Months.entries[java.time.LocalDate.now().monthValue - 1]
            if (args.month == currentMonth) {
                mbAdd.visibility = View.VISIBLE
                mbAdd.setOnClickListener {
                    val action = ExpenseDetailFragmentDirections
                        .actionExpenseDetailFragmentToAddExpenseFragment()
                    findNavController().navigate(action)
                }
            } else {
                mbAdd.visibility = View.GONE
            }
        }
    }

    private fun setupRecyclerView() {
        binding.rvExpenses.adapter = adapter
        binding.rvExpenses.layoutManager = LinearLayoutManager(requireContext())
    }


    suspend fun getExpense() {
        val expenses = viewModel.getExpensesByMonth(args.month)

        if (expenses.isNotEmpty()) {
            expense = expenses.first()
            adapter.setExpenses(expenses)
            setupRecyclerView()
            calculateCategoryTotals(expenses)
        } else {
            showNoData()
        }
    }

    fun setSort(sortBy: SortBy, orderBy: SortOrder) {
        currentSort = sortBy
        currentOrder = orderBy
        viewModel.setSorting(sortBy, orderBy)
    }

    fun setTextListener() {
        binding.etSearch.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                viewModel.setSearch(p0.toString())
                lifecycleScope.launch { getExpense() }
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        })
    }

    private fun calculateCategoryTotals(expenses: List<Expense>) {
        var entertainmentTotal = 0.0
        var shopsTotal = 0.0
        var foodTotal = 0.0
        var othersTotal = 0.0
        for (expense in expenses) {
            when (expense.category) {
                Category.ENTERTAINMENT -> entertainmentTotal += expense.price
                Category.SHOPS -> shopsTotal += expense.price
                Category.FOOD_AND_DRINK -> foodTotal += expense.price
                Category.OTHERS -> othersTotal += expense.price
            }
        }
        val grandTotal = entertainmentTotal + shopsTotal + foodTotal + othersTotal
        // Update UI
        binding.tvTotal.text = String.format("Total: RM%.2f", grandTotal)
        binding.tvEntertainmentTotal.text = String.format("Entertainment: RM%.2f", entertainmentTotal)
        binding.tvShopsTotal.text = String.format("Shops: RM%.2f", shopsTotal)
        binding.tvFoodTotal.text = String.format("Food and Drink: RM%.2f", foodTotal)
        binding.tvOthersTotal.text = String.format("Others: RM%.2f", othersTotal)

        // After updating text totals
        setupPieChart(entertainmentTotal, shopsTotal, foodTotal, othersTotal)
    }

    private fun setupPieChart(entertainment: Double, shops: Double, food: Double, others: Double) {
        val entries = createPieEntries(entertainment, shops, food, others)
        val dataSet = PieDataSet(entries, "").apply {
            colors = ColorTemplate.MATERIAL_COLORS.toList()
            sliceSpace = 3f
            selectionShift = 5f
        }

        val data = PieData(dataSet).apply {
            setValueTextSize(12f)
            setValueTextColor(Color.WHITE)
        }

        configurePieChart(data)
    }

    private fun createPieEntries(entertainment: Double, shops: Double, food: Double, others: Double): ArrayList<PieEntry> {
        val entries = ArrayList<PieEntry>()
        if (entertainment > 0) entries.add(PieEntry(entertainment.toFloat(), "Entertainment"))
        if (shops > 0) entries.add(PieEntry(shops.toFloat(), "Shops"))
        if (food > 0) entries.add(PieEntry(food.toFloat(), "Food & Drink"))
        if (others > 0) entries.add(PieEntry(others.toFloat(), "Others"))
        return entries
    }

    private fun configurePieChart(data: PieData) {
        binding.pieChart.apply {
            this.data = data
            description.isEnabled = false
            setUsePercentValues(true)
            isDrawHoleEnabled = true
            setHoleColor(Color.TRANSPARENT)
            setEntryLabelColor(Color.BLACK)
            centerText = "Expenses"
            setCenterTextSize(14f)

            legend.orientation = Legend.LegendOrientation.HORIZONTAL
            legend.isWordWrapEnabled = true
            legend.horizontalAlignment = Legend.LegendHorizontalAlignment.CENTER
            legend.verticalAlignment = Legend.LegendVerticalAlignment.BOTTOM

            animateY(1000)
            invalidate()
        }
    }





    fun createDeleteDialog(expenseId: Int?): Dialog {
        return Dialog(requireContext()).apply {
            setContentView(R.layout.confirmation_dialog)
            window?.setBackgroundDrawable(Color.TRANSPARENT.toDrawable())
            findViewById<MaterialButton>(R.id.mbCancel).setOnClickListener { dismiss() }
            findViewById<MaterialButton>(R.id.mbConfirm).setOnClickListener {
                viewModel.deleteExpense(expenseId)
                lifecycleScope.launch { getExpense() }
                dismiss()
            }
        }
    }

    fun setNavigation() {
        binding.ivSort.setOnClickListener {
            val dialog = SortDialogFragment(currentSort, currentOrder) { sortBy, orderBy, category ->
                setSort(sortBy, orderBy)
                viewModel.setCategoryFilter(category)
                lifecycleScope.launch { getExpense() } // refresh list
            }
            dialog.show(parentFragmentManager, Constant.SORTING_DIALOG)
        }
    }
}