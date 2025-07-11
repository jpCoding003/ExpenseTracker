package com.tops.expensetracker.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ExpenseRoot(
    val id: Int,
    val title: String,
    val amount: String,
    val category: String,
    val date: String
): Parcelable
