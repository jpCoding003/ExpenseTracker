package com.tops.expensetracker.fragments

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import com.tops.expensetracker.R
import com.tops.expensetracker.databinding.FragmentAddNewExpenseBinding


class AddNewExpenseFragment : Fragment() {

    private lateinit var binding: FragmentAddNewExpenseBinding
    private lateinit var  db: SQLiteDatabase


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentAddNewExpenseBinding.inflate(layoutInflater)
        (activity as AppCompatActivity).supportActionBar?.setTitle("Add New Expenses")
        db = requireActivity().openOrCreateDatabase("expensetracker", Context.MODE_PRIVATE, null)
//        db.execSQL("DROP TABLE IF EXISTS  expense")
        db.execSQL("CREATE TABLE IF NOT EXISTS expense(ID INTEGER PRIMARY KEY AUTOINCREMENT, TITLE VARCHAR, AMOUNT VARCHAR, CATEGORY VARCHAR,DATE VARCHAR);")
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        binding.btnaddExpense.setOnClickListener {
            val title = binding.etTitle.text.toString()
            val amount = binding.etAmount.text.toString()
            val expenseCategory = binding.etCategory.text.toString()
            val date = binding.etDate.text.toString()
//            if (title.isEmpty() || amount.isEmpty() || expenseCategory.isEmpty() || date.isEmpty()){
//                Toast.makeText(context," Please Fill Complete Details!", Toast.LENGTH_LONG).show()
//            }else{
                val contentValue = ContentValues()

                contentValue.put("TITLE", title)
                contentValue.put("AMOUNT", amount)
                contentValue.put("CATEGORY", expenseCategory)
                contentValue.put("DATE", date)

            val result = db.insert("expense" ,null,contentValue)

            if (result != -1L) {
                Toast.makeText(context, "Insert successful", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "Insert failed", Toast.LENGTH_SHORT).show()
            }

            Log.i(" DataBase"," Data ${db}")
                findNavController().navigate(R.id.action_addNewExpenseFragment_to_homeFragment)

//            }

        }
    }

}