package com.tops.expensetracker.viewmodel

import android.app.DatePickerDialog
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tops.expensetracker.model.ExpenseRoot
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class MonthlyExpenseViewModel: ViewModel() {


    private val calendar = Calendar.getInstance()

    private val _expense = MutableLiveData<List<ExpenseRoot>>()
    val expense : LiveData<List<ExpenseRoot>> = _expense

    private val _dateselected = MutableLiveData<String>()
    val dateselected: LiveData<String> = _dateselected


    fun loadExpensedata(context: Context, startDate: String, endDate: String){

        val db = context.openOrCreateDatabase("expensetracker", Context.MODE_PRIVATE, null)
        val cursor = db.rawQuery("SELECT * FROM expense WHERE date BETWEEN ? AND ?", arrayOf(startDate,endDate))
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
                    date = date // ✅ Correct
                )
                expenselist.add(exp)

            }while (cursor.moveToNext())
        }
        cursor.close()
        _expense.value = expenselist
    }

    fun deleteExpense(context: Context,expenseId: Int,startDate: String, endDate: String){
        val db = context.openOrCreateDatabase("expensetracker", Context.MODE_PRIVATE, null)
        db.delete("expense","id = ?",arrayOf(expenseId.toString()))
        loadExpensedata(context,startDate, endDate)
    }



   fun ShowDatePiccker(context: Context){
        val datePickerDialog = DatePickerDialog(
            context, { DatePicker, year: Int, monthOfYear: Int, dayOfMonth: Int ->

                val selectedDate = Calendar.getInstance()

                selectedDate.set(year, monthOfYear, dayOfMonth)

                val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

                val formattedDate = dateFormat.format(selectedDate.time)

                _dateselected.value = formattedDate
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        // Show the DatePicker dialog
        datePickerDialog.show()
    }
}