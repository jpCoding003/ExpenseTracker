package com.tops.expensetracker.fragments

import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.tops.expensetracker.R
import com.tops.expensetracker.adapter.MyAdapter
import com.tops.expensetracker.databinding.FragmentMonthlyExpenseBinding
import com.tops.expensetracker.viewmodel.MonthlyExpenseViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


class MonthlyExpenseFragment : Fragment() {

    private var selectedStartDate: String = ""
    private var selectedEndDate: String = ""

    private lateinit var binding: FragmentMonthlyExpenseBinding
    private lateinit var adapter: MyAdapter
    private  val monthlyexpenseviewmodel: MonthlyExpenseViewModel by viewModels()



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMonthlyExpenseBinding.inflate(layoutInflater)
        (activity as AppCompatActivity).supportActionBar?.setTitle("Filter Monthly")
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tvfromSelectedDate.setOnClickListener {
            // Show the DatePicker dialog
//            monthlyexpenseviewmodel.ShowDatePiccker(requireActivity())
//            monthlyexpenseviewmodel.dateselected.observe(viewLifecycleOwner){
//                    date->
//                selectedStartDate = date
//                binding.tvfromSelectedDate.text = date
//            }
            showDatePicker(requireActivity()){ date ->
                selectedStartDate = date
                binding.tvfromSelectedDate.text = date
            }
        }

        binding.tvUptoSelectedDate.setOnClickListener {
            // Show the DatePicker dialog
//            monthlyexpenseviewmodel.ShowDatePiccker(requireActivity())
//            monthlyexpenseviewmodel.dateselected.observe(viewLifecycleOwner){
//                    date->
//                selectedEndDate = date
//                binding.tvUptoSelectedDate.text = date
//            }
            showDatePicker(requireActivity()) { date ->
                selectedEndDate = date
                binding.tvUptoSelectedDate.text = date
            }

        }

        binding.btnshow.setOnClickListener {
            val startDate = binding.tvfromSelectedDate.text.toString()
            val endDate = binding.tvUptoSelectedDate.text.toString()
            if (startDate.isNotEmpty() && endDate.isNotEmpty()) {
                monthlyexpenseviewmodel.loadExpensedata(requireContext(), startDate, endDate)
            }
        }


        binding.rvMonthlyTrans.layoutManager = LinearLayoutManager(requireContext())
        adapter = MyAdapter(emptyList(),
            onDelete = { id-> monthlyexpenseviewmodel.deleteExpense(requireContext(),id,selectedStartDate, selectedEndDate)},
            onEdit = { expense-> val bundle = Bundle().apply {
                putParcelable("expense",expense)                // Bundle--> Data Passed from Fragment to Fragment
            }

                findNavController().navigate(R.id.action_homeFragment_to_addNewExpenseFragment, bundle)
            })
        // ✅ Set adapter to RecyclerView
        binding.rvMonthlyTrans.adapter = adapter

        monthlyexpenseviewmodel.expense.observe(viewLifecycleOwner, Observer{
                expenselist->  adapter.submitList(expenselist)
        })




    }
    fun showDatePicker(context: Context, onDateSelected: (String) -> Unit) {
        val calendar = Calendar.getInstance()
        val datePickerDialog = DatePickerDialog(
            context,
            { _, year, month, day ->
                val selected = Calendar.getInstance()
                selected.set(year, month, day)
                val formattedDate =
                    SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(selected.time)
                onDateSelected(formattedDate)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.show()
    }

}