package com.leong.expensesrecorder.ui.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController

class HomeFragment : BaseHomeManageFragment() {
    override val viewModel: HomeViewModel by viewModels{
        HomeViewModel.Factory
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupAdapter()
        binding.fabAdd.setOnClickListener {
            val action = HomeFragmentDirections.actionHomeFragmentToAddExpenseFragment()
            findNavController().navigate(action)
        }

        binding.tvEmpty.text = "You have no new expenses"
    }

}