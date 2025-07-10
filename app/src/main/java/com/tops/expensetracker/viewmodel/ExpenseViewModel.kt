package com.tops.expensetracker.viewmodel

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tops.expensetracker.model.ExpenseRoot


class ExpenseViewModel: ViewModel() {

    private lateinit var db: SQLiteDatabase
    private val _expense = MutableLiveData<List<ExpenseRoot>>()
    val expense : LiveData<List<ExpenseRoot>> = _expense

    fun loadExpensedata(context: Context){
        val db: SQLiteDatabase = context.openOrCreateDatabase("expensetracker", Context.MODE_PRIVATE, null)
        val cursor = db.rawQuery(" SELECT * FROM expense", null)
        val expenselist = mutableListOf<ExpenseRoot>()

        if (cursor.moveToFirst()){
            do{
                val id = cursor.getInt(0)
                val title = cursor.getString(1)
                val amount = cursor.getString(2)
                val category = cursor.getString(3)
                val date = cursor.getString(4)

                val exp = ExpenseRoot(id,title,amount,category,date)
                expenselist.add(exp)

            }while (cursor.moveToNext())
        }
        cursor.close()
        _expense.value = expenselist
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
        contentvalue.put("DATE", expense.date)

        db.update("expense",contentvalue,"ID=?", arrayOf(expense.id.toString()))
        loadExpensedata(context)
    }
}