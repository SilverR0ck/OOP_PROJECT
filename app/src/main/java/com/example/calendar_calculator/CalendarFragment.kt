package com.example.calendar_calculator

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
class CalendarFragment : Fragment() {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val buttonCalculate = view.findViewById<Button>(R.id.buttonCalculate)
        val hourlyRateEditText = view.findViewById<EditText>(R.id.hourlyRateEditText)
        val daysWorkedEditText = view.findViewById<EditText>(R.id.daysWorkedEditText)
        val resultTextView = view.findViewById<TextView>(R.id.resultTextView)

        // "계산하기" 버튼 클릭 시 월급 계산
        buttonCalculate.setOnClickListener {
            val hourlyRate = hourlyRateEditText.text.toString().toDouble()
            val daysWorked = daysWorkedEditText.text.toString().toDouble()

            val monthlyEarnings = hourlyRate * daysWorked * 8 * 4 // 1주일 5일, 1일 8시간, 1달 4주로 가정
            resultTextView.text = "월급: $monthlyEarnings 원"
        }
    }
}
