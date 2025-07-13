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
import com.tops.expensetracker.model.ExpenseRoot
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class AddNewExpenseFragment : Fragment() {

    private lateinit var binding: FragmentAddNewExpenseBinding
    private lateinit var  db: SQLiteDatabase

    private var expenseId: Int = -1 // ✅ Hold expenseId for edit mode

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentAddNewExpenseBinding.inflate(layoutInflater)
        (activity as AppCompatActivity).supportActionBar?.setTitle("Add New Expenses")

        db = requireActivity().openOrCreateDatabase("expensetracker", Context.MODE_PRIVATE, null)
//        db.execSQL("DROP TABLE IF EXISTS  expense")
        db.execSQL("CREATE TABLE IF NOT EXISTS expense(ID INTEGER PRIMARY KEY AUTOINCREMENT, TITLE VARCHAR, AMOUNT VARCHAR, CATEGORY VARCHAR,DATE  TEXT DEFAULT (datetime('now','localtime')));")
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.etDate.visibility = View.GONE

        // ✅ Get data from Bundle
        val expense = arguments?.getParcelable<ExpenseRoot>("expense")
        val isEditMode = expense != null

        // ✅ Set data in EditText if edit mode
        if (isEditMode) {
            binding.etTitle.setText(expense.title)
            binding.etAmount.setText(expense.amount)
            binding.etCategory.setText(expense.category)
            binding.etDate.setText(expense.date)
            binding.btnaddExpense.text = "Update Expense"
            expenseId = expense?.id?: -1
        }


        binding.btnaddExpense.setOnClickListener {
            val today = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(Date())
            val title = binding.etTitle.text.toString()
            val amount = binding.etAmount.text.toString()
            val expenseCategory = binding.etCategory.text.toString()

            if (title.isEmpty() || amount.isEmpty() || expenseCategory.isEmpty() ){
                Toast.makeText(context," Please Fill Complete Details!", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }else{
                val contentValue = ContentValues().apply {
                    put("TITLE", title)
                    put("AMOUNT", amount)
                    put("CATEGORY", expenseCategory)
                    put("DATE", today)
                }


                if (isEditMode && expenseId!= -1) {
                    // ✅ Update expense
                    val rows = db.update("expense", contentValue, "ID = ?", arrayOf(expenseId.toString()))
                    if (rows > 0)
                        Toast.makeText(context, "Expense updated", Toast.LENGTH_SHORT).show()
                    else
                        Toast.makeText(context, "Update failed", Toast.LENGTH_SHORT).show()
                } else {
                    // ✅ Insert new expense
                    val result = db.insert("expense", null, contentValue)
                    if (result != -1L)
                        Toast.makeText(context, "Expense added", Toast.LENGTH_SHORT).show()
                    else
                        Toast.makeText(context, "Insert failed", Toast.LENGTH_SHORT).show()
                }


                findNavController().navigate(R.id.action_addNewExpenseFragment_to_homeFragment)
            }
        }
    }
}