package com.tops.expensetracker.fragments

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

        expenseviewmodel.loadExpensedata(requireContext())

        binding.rvExpense.layoutManager = LinearLayoutManager(requireContext())
        adapter = MyAdapter(emptyList(), onDelete = {
            id-> expenseviewmodel.deleteExpense(requireContext(),id)
        }, onEdit = { expense->
//            val action = HomeFragmentDirections.actionHomeFragmentToAddNewExpenseFragment(
//                expense.id,
//                expense.title,
//                expense.amount,
//                expense.category,
//                expense.date
//            )
//            findNavController().navigate(action)

            val bundle = Bundle().apply {
                putInt("expenseId", expense.id)
                putString("title", expense.title)
                putString("amount", expense.amount)
                putString("category", expense.category)
                putString("date", expense.date)
            }

            findNavController().navigate(R.id.action_homeFragment_to_addNewExpenseFragment, bundle)
        })

        expenseviewmodel.expense.observe(viewLifecycleOwner, Observer{
            expenselist->  adapter.submitList(expenselist)
        })

        binding.btnaddExp.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_addNewExpenseFragment)
        }
    }


}