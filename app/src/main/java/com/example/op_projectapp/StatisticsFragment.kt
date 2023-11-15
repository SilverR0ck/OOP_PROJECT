package com.example.op_projectapp

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry

class StatisticsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_statistics, container, false)

        // Get the BarChart view from the layout
        val barChart: BarChart = view.findViewById(R.id.statisticsBarChart)

        // Create dummy data for the bar chart
        val entries = listOf(
            BarEntry(1f, 60f, "7월"),
            BarEntry(2f, 70f, "8월"),
            BarEntry(3f, 30f, "9월"),
            BarEntry(4f, 15f, "10월"),
            BarEntry(5f, 100f, "11월")
        )

        // Create a BarDataSet with the dummy data
        val dataSet = BarDataSet(entries, "최근 5개월 간 소득 추이")
        dataSet.color = Color. BLACK

        // Create a BarData object with the BarDataSet
        val barData = BarData(dataSet)

        // Customize the X-axis
        val xAxis: XAxis = barChart.xAxis
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.setDrawGridLines(false)

        // Set the bar chart data
        barChart.data = barData

        // Refresh the chart
        barChart.invalidate()

        return view
    }
}