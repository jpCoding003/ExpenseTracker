package com.tops.expensetracker.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tops.expensetracker.databinding.ExpenseRowItemBinding
import com.tops.expensetracker.model.ExpenseRoot

class MyAdapter(private val expense: List<ExpenseRoot>): RecyclerView.Adapter<MyAdapter.ExpenseViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ExpenseViewHolder {
        val binding = ExpenseRowItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ExpenseViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: ExpenseViewHolder,
        position: Int
    ) {
        val exp = expense[position]
        holder.binding.tvtitle.text = exp.title
        holder.binding.tvprice.text = "$${exp.amount}"
    }

    override fun getItemCount(): Int = expense.size

    class ExpenseViewHolder(val binding : ExpenseRowItemBinding): RecyclerView.ViewHolder(binding.root)
}