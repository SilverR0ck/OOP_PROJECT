package com.example.op_projectapp

import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.op_projectapp.databinding.FragmentStatisticsBinding
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import java.util.Calendar

class StatisticsFragment : Fragment() {

    private var _binding: FragmentStatisticsBinding? = null
    private val binding get() = _binding!!

    private lateinit var placeViewModel: PlaceViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStatisticsBinding.inflate(inflater, container, false)
        placeViewModel = ViewModelProvider(this).get(PlaceViewModel::class.java)

        setupBarChart()

        placeViewModel.nameplace.observe(viewLifecycleOwner, { places ->
            places?.let {
                setupSpinnerAdapter(it)
                setupSpinnerItemSelectedListener(it)
            }
        })

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupBarChart() {
        val xAxis: XAxis = binding.statisticsBarChart.xAxis
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.setDrawGridLines(false)
        xAxis.valueFormatter = IndexAxisValueFormatter(getMonths()) // X축 레이블 설정
        xAxis.setGranularity(1f)

        val yAxisLeft = binding.statisticsBarChart.axisLeft
        yAxisLeft.setTypeface(Typeface.DEFAULT_BOLD) // 글꼴을 두껍게 설정
        yAxisLeft.axisMinimum = 0f // 최소값
        yAxisLeft.axisMaximum = 2000000f // 최대값
        yAxisLeft.setDrawGridLines(true)

        val yAxisRight = binding.statisticsBarChart.axisRight
        yAxisRight.isEnabled = false // 오른쪽 Y축 비활성화
    }

    private fun getMonths(): ArrayList<String> {
        val now = Calendar.getInstance()
        return arrayListOf(
            getMonth(now, -4),
            getMonth(now, -3),
            getMonth(now, -2),
            getMonth(now, -1),
            getMonth(now, 0)
        )
    }

    // 특정 달을 계산하여 문자열로 반환하는 함수
    private fun getMonth(calendar: Calendar, offset: Int): String {
        val newCalendar = calendar.clone() as Calendar // 복사본 생성
        newCalendar.add(Calendar.MONTH, offset) // 복사본에서 월 변경
        val month = newCalendar.get(Calendar.MONTH) + 1
        return "$month 월"
    }

    private fun setupSpinnerAdapter(places: List<Place>) {
        val placeNames = places.map { it.name ?: "" }
        val arrayAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, placeNames)
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.placeSpinner.adapter = arrayAdapter
    }

    private fun setupSpinnerItemSelectedListener(places: List<Place>) {
        binding.placeSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                updateBarChartData(places[position])
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Do nothing
            }
        }
    }


    private fun calculateSalaries(place: Place): List<BarEntry> {
        val salaries = place.salary?.takeLast(5)?.toMutableList() ?: mutableListOf()
        while (salaries.size < 5) {
            salaries.add(0, 0) // 부족한 항목을 0으로 채움
        }
        return salaries.mapIndexed { index, salary ->
            BarEntry(index.toFloat(), salary.toFloat())
        }
    }


    private fun createDataSet(place: Place): BarDataSet {
        val entries = calculateSalaries(place)
        return BarDataSet(entries, "${place.name} 최근 5개월 간 소득 추이").apply {
            color = Color.BLUE
            setValueTextSize(10f)
        }
    }

    private fun updateBarChartData(place: Place) {
        val dataSet = createDataSet(place)
        val barData = BarData(dataSet)
        binding.statisticsBarChart.data = barData
        binding.statisticsBarChart.invalidate()
    }
}
