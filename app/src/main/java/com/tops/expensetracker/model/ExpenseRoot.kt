package com.tops.expensetracker.model

data class ExpenseRoot(
    val id: Int,
    val title: String,
    val amount: String,
    val category: String,
    val date: String
)
