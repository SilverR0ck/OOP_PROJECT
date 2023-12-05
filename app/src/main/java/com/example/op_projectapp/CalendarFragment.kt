package com.example.op_projectapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.op_projectapp.databinding.FragmentCalendarBinding
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter

class CalendarFragment : Fragment() {
    var binding: FragmentCalendarBinding? = null
    private val viewModel : PlaceViewModel by viewModels()
    private lateinit var checkedDate: LocalDate

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentCalendarBinding.inflate(inflater)

        return binding?.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        checkedDate = LocalDate.now()

        setMonth()

        //다음달 달력 세팅
        binding?.btnLastMonth?.setOnClickListener{
            checkedDate=checkedDate.minusMonths(1)
            setMonth()
        }

        //저번달 달력 세팅
        binding?.btnNextMonth?.setOnClickListener{
            checkedDate=checkedDate.plusMonths(1)
            setMonth()
        }

    }
    private fun setMonth(){
        binding?.txtMonthView?.text=formattedMonth(checkedDate)

        val dateList= monthDayArray(checkedDate)

        //layoutManager, adapter 설정
        binding?.ryCalendar?.layoutManager=GridLayoutManager(requireContext(), 7)
        binding?.ryEvents?.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        val calendarAdapter=CalendarAdapter(dateList)
        binding?.ryCalendar?.adapter=calendarAdapter


        val eventsAdapter = EventsAdapter(viewModel.nameplace)
        binding?.ryEvents?.adapter=EventsAdapter(viewModel.nameplace)

        viewModel.nameplace.observe(viewLifecycleOwner){
            eventsAdapter.notifyDataSetChanged()
        }

    }

    private fun formattedMonth(date: LocalDate):String {
        var formatter = DateTimeFormatter.ofPattern("MM월")
        return date.format(formatter)
    }

    private fun monthDayArray(date: LocalDate): ArrayList<String>{
        val dateList=ArrayList<String>()
        val monthInfo= YearMonth.from(date)
        val lastDayOfMonth=monthInfo.lengthOfMonth()
        val firstDayOfMonth = checkedDate.withDayOfMonth(1)
        val firstDayOfWeek = firstDayOfMonth.dayOfWeek.value

        for(i in 1..41){
            if(i <= firstDayOfWeek || i > (lastDayOfMonth + firstDayOfWeek)){
                dateList.add("")
            }else{
                dateList.add((i - firstDayOfWeek).toString())
            }
        }
        return dateList
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

}