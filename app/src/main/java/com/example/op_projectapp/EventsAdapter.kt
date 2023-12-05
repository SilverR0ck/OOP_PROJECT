package com.example.op_projectapp

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import com.example.op_projectapp.databinding.ItemEventsBinding

class EventsAdapter (private val workList: LiveData<List<Place>>):
    RecyclerView.Adapter<EventsAdapter.EventViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventsAdapter.EventViewHolder {
        val binding = ItemEventsBinding.inflate(LayoutInflater.from(parent.context))
        return EventViewHolder(binding)
    }

    override fun onBindViewHolder(holder: EventsAdapter.EventViewHolder, position: Int) {
        val placeList = workList.value ?: return
        val place: Place = placeList[position]

        // Check if the event's dayCalendarCheck matches the selected days
        val selectedDays = place.dayCalendarCheck
        if (selectedDays?.contains(1) == true) {
            holder.eventName.text = place.name
            holder.eventStart.text = place.starttime
            holder.eventEnd.text = place.endtime

            val daysOfWeek = arrayOf("월", "화", "수", "목", "금", "토", "일")
            val selectedDays = place.dayCalendarCheck

            val formattedDays = StringBuilder()
            for (i in daysOfWeek.indices) {
                if (selectedDays?.get(i) == 1) {
                    if (formattedDays.isNotEmpty()) {
                        formattedDays.append(", ")
                    }
                    formattedDays.append(daysOfWeek[i])
                }
            }

            holder.eventDay.text = formattedDays.toString()
        }
    }
    override fun getItemCount() = workList.value?.size ?: 0

    inner class EventViewHolder(binding:ItemEventsBinding): RecyclerView.ViewHolder(binding.root) {

        val eventName: TextView  = binding.txtEventName //일정 이름
        val eventStart: TextView = binding.txtEventStart //일정 시작 시간
        val eventEnd: TextView = binding.txtEventEnd //일정 종료 시간
        val eventDay: TextView = binding.txtDay //일정 요일
    }

}