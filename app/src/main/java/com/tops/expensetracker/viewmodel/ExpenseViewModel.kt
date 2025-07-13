package com.tops.expensetracker.viewmodel

import android.content.ContentValues
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tops.expensetracker.model.ExpenseRoot
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class ExpenseViewModel: ViewModel() {

//    private lateinit var db: SQLiteDatabase
    private val _expense = MutableLiveData<List<ExpenseRoot>>()
    val expense : LiveData<List<ExpenseRoot>> = _expense

    private val _totalAmount = MutableLiveData<Int>()
    val totalAmount: LiveData<Int> = _totalAmount


    fun loadExpensedata(context: Context){
        val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        val db = context.openOrCreateDatabase("expensetracker", Context.MODE_PRIVATE, null)
        val cursor = db.rawQuery(" SELECT * FROM expense", null)
        val expenselist = mutableListOf<ExpenseRoot>()

        if (cursor.moveToFirst()){
            do{
                val id = cursor.getInt(0)
                val title = cursor.getString(1)
                val amount = cursor.getString(2)
                val category = cursor.getString(3)
                val date = cursor.getString(4)

                val exp = ExpenseRoot(
                    id, title, amount, category,
                    date = today
                )
                expenselist.add(exp)

            }while (cursor.moveToNext())
        }
        cursor.close()
        _expense.value = expenselist

        // ✅ Compute total and update LiveData
        val total = expenselist.sumOf { it.amount.toIntOrNull() ?: 0 }
        _totalAmount.value = total
    }

    fun deleteExpense(context: Context,expenseId: Int){
        val db = context.openOrCreateDatabase("expensetracker", Context.MODE_PRIVATE, null)
        db.delete("expense","id = ?",arrayOf(expenseId.toString()))
        loadExpensedata(context)
    }

    fun updateExpense(context: Context,expense : ExpenseRoot){
        val db = context.openOrCreateDatabase("expensetracker", Context.MODE_PRIVATE, null)
        val contentvalue= ContentValues()
        contentvalue.put("TITLE", expense.title)
        contentvalue.put("AMOUNT", expense.amount)
        contentvalue.put("CATEGORY", expense.category)

        db.update("expense",contentvalue,"ID=?", arrayOf(expense.id.toString()))
        loadExpensedata(context)
    }


}