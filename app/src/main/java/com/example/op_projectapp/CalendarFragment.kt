package com.example.op_projectapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CalendarView
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.op_projectapp.databinding.FragmentCalendarBinding
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.Calendar


class CalendarFragment : Fragment() {
    var binding : FragmentCalendarBinding? = null
    val viewModel : PlaceViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCalendarBinding.inflate(inflater)
        val view = inflater.inflate(R.layout.fragment_calendar, container, false)
        val recEvents = view.findViewById<RecyclerView>(R.id.rec_Events)
        val calendarView = view.findViewById<CalendarView>(R.id.calendarView)

        recEvents.layoutManager = LinearLayoutManager(this)
        recEvents.adapter

        val eventList= arrayListOf(
            //데이터 가져오기
        )
        rv_eventList.layouyManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        re_eventList.setHasFixedSize(true)
        rv_eventList.adapter=EventsAdapter

        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.nameplace.observe(viewLifecycleOwner){
            viewModel.nameplace.value
        }
    }
}

