package com.example.op_projectapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView

class EventsAdapter (private val worklist: LiveData<List<Place>>): RecyclerView.Adapter<EventsAdapter.EventViewHolder>() {

    private var placeList = mutableListOf<Place>()
    fun setData(data: MutableLiveData<Place>){
        placeList = data
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventsAdapter.EventViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_events, parent, false)
        return EventViewHolder(view)
    }

    override fun onBindViewHolder(holder: EventsAdapter.EventViewHolder, position: Int) {
        val place:Place = placeList[position]
        holder.eventName.text = place.name
        holder.eventStart.text = place.starttime
        holder.eventEnd.text = place.endtime

    }

    override fun getItemCount(): Int {
        return worklist.value?.size ?: 0
    }

    inner class EventViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val eventName: TextView  = itemView.findViewById(R.id.txt_eventName) //일정 이름
        val eventStart: TextView = itemView.findViewById(R.id.txt_eventStart) //일정 시작 시간
        val eventEnd: TextView = itemView.findViewById(R.id.txt_eventEnd) //일정 종료 시간
    }

}