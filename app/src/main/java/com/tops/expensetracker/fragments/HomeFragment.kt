package com.tops.expensetracker.fragments

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.tops.expensetracker.R
import com.tops.expensetracker.adapter.MyAdapter
import com.tops.expensetracker.databinding.ActivityMainBinding
import com.tops.expensetracker.databinding.FragmentHomeBinding
import com.tops.expensetracker.model.ExpenseRoot
import com.tops.expensetracker.viewmodel.ExpenseViewModel
import kotlin.math.exp


class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var adapter: MyAdapter
    private val expenseviewmodel: ExpenseViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(layoutInflater)
        (activity as AppCompatActivity).supportActionBar?.setTitle("Home")
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        expenseviewmodel.loadExpensedata(requireActivity())
        expenseviewmodel.totalAmount.observe(viewLifecycleOwner) { total ->
            binding.tvTotal.text = "₹ $total"
        }

        binding.rvExpense.layoutManager = LinearLayoutManager(requireContext())
        adapter = MyAdapter(emptyList(),
            onDelete = { id-> expenseviewmodel.deleteExpense(requireContext(),id) },
            onEdit = { expense-> val bundle = Bundle().apply {
                putParcelable("expense",expense)                // Bundle--> Data Passed from Fragment to Fragment
            }

            findNavController().navigate(R.id.action_homeFragment_to_addNewExpenseFragment, bundle)
        })
        // ✅ Set adapter to RecyclerView
        binding.rvExpense.adapter = adapter

        expenseviewmodel.expense.observe(viewLifecycleOwner, Observer{
            expenselist->  adapter.submitList(expenselist)
        })

        binding.btnaddExp.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_addNewExpenseFragment)
        }

//        val currentDate = expenseviewmodel.storecurrentdate(requireContext())
//        binding.tvTotal.text = currentDate.toString()
    }


}