package com.leong.expensesrecorder.ui.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import com.leong.expensesrecorder.data.enums.Months

class HomeFragment : BaseHomeManageFragment() {
    override val viewModel: HomeViewModel by viewModels{
        HomeViewModel.Factory
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.fabAdd.setOnClickListener {
            val action = HomeFragmentDirections.actionHomeFragmentToAddExpenseFragment()
            findNavController().navigate(action)
        }
    }

    override fun getExpenseDetailAction(months: Months): NavDirections {
        return HomeFragmentDirections.actionHomeFragmentToExpenseDetailFragment(months)
    }

}