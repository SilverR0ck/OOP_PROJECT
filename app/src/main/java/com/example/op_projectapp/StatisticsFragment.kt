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
import com.example.op_projectapp.Model.Place
import com.example.op_projectapp.databinding.FragmentStatisticsBinding
import com.example.op_projectapp.viewModel.PlaceViewModel
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import java.util.Calendar

class StatisticsFragment : Fragment() {

    private var _binding: FragmentStatisticsBinding? = null
    private val binding: FragmentStatisticsBinding
        get() = _binding ?: throw IllegalStateException("바인딩이 초기화 되지 않음")

    private lateinit var placeViewModel: PlaceViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStatisticsBinding.inflate(layoutInflater)
        placeViewModel = ViewModelProvider(this).get(PlaceViewModel::class.java)

        // 바 차트 설정 초기화
        setupBarChart()

        // ViewModel에서 관찰 중인 데이터의 변화를 감지하고 새로운 데이터로 Spinner 및 Bar Chart를 업데이트
        placeViewModel.places.observe(viewLifecycleOwner, { places ->
            places?.let {
                // Spinner에 장소 이름들을 설정
                setupSpinnerAdapter(it)

                // Spinner의 선택 변경 리스너 설정
                setupSpinnerItemSelectedListener(it)
            }
        })
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    // 바 차트 설정 초기화 함수
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

    // 최근 5개월의 월 이름을 반환하는 함수
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

    // Spinner 어댑터 설정 함수
    private fun setupSpinnerAdapter(places: List<Place>) {
        val placeNames = places.map { it.name ?: "" }
        val arrayAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, placeNames)
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.placeSpinner.adapter = arrayAdapter
    }

    // Spinner 선택 리스너 설정 함수
    private fun setupSpinnerItemSelectedListener(places: List<Place>) {
        binding.placeSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                // 선택된 장소에 대한 데이터로 Bar Chart를 업데이트
                updateBarChartData(places[position])
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Do nothing
            }
        }
    }

    // 선택된 장소의 최근 5개월 간 소득 데이터를 계산하는 함수
    private fun calculateSalaries(place: Place): List<BarEntry> {
        val salaries = place.salary?.takeLast(5)?.toMutableList() ?: mutableListOf()
        while (salaries.size < 5) {
            salaries.add(0, 0) // 부족한 항목을 0으로 채움
        }
        return salaries.mapIndexed { index, salary ->
            BarEntry(index.toFloat(), salary.toFloat())
        }
    }

    // 선택된 장소의 BarDataSet을 생성하는 함수
    private fun createDataSet(place: Place): BarDataSet {
        val entries = calculateSalaries(place)
        return BarDataSet(entries, "${place.name} 최근 5개월 간 소득 추이").apply {
            color = Color.BLUE
            setValueTextSize(10f)
        }
    }

    // Bar Chart 데이터를 업데이트하는 함수
    private fun updateBarChartData(place: Place) {
        val dataSet = createDataSet(place)
        val barData = BarData(dataSet)
        binding.statisticsBarChart.data = barData
        binding.statisticsBarChart.invalidate()
    }
}
