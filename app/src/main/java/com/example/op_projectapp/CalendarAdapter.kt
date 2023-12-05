package com.example.op_projectapp

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.op_projectapp.databinding.ItemCalendarBinding

class CalendarAdapter(private val dateList: ArrayList<String>) : RecyclerView.Adapter<CalendarAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder{
        val binding = ItemCalendarBinding.inflate(LayoutInflater.from(parent.context))
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.date.text = dateList[holder.adapterPosition]
    }

    override fun getItemCount() = dateList.size

    inner class ViewHolder(binding:ItemCalendarBinding) : RecyclerView.ViewHolder(binding.root) {
        val date: TextView = binding.txtDate
    }
}