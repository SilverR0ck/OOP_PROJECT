package com.example.calendar_calculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 초기 프래그먼트로 CalendarFragment를 표시
        val calendarFragment = CalendarFragment()
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, calendarFragment)
            .commit()
        // XML 레이아웃에서 정의한 버튼의 ID를 참조합니다.
        val buttonCalendar = findViewById<Button>(R.id.buttonCalendar)
        // "캘린더 화면" 버튼 클릭 시 CalendarFragment로 전환
        buttonCalendar.setOnClickListener {
            val newFragment = CalendarFragment()
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, newFragment)
                .commit()
        }

        //r

        val buttonCalculator = findViewById<Button>(R.id.buttonCalendar)
        // "계산기 화면" 버튼 클릭 시 CalculatorFragment로 전환
        buttonCalculator.setOnClickListener {
            val newFragment = CalculatorFragment()

            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, newFragment)
                .commit()

            }

    }
}