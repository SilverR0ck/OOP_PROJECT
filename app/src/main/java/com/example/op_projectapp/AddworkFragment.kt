package com.example.op_projectapp

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import androidx.fragment.app.viewModels


class AddworkFragment : Fragment() {

    // viewModel PlaceViewModel 인스턴스 생성
    private val viewModel: PlaceViewModel by viewModels()

    // AddworkFragment의 뷰를 생성 및 초기화
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // AddWork뷰 inflate하고 뷰 관련 설정
        return inflater.inflate(R.layout.fragment_addwork, container, false).apply {
            val work_placenameEditText = findViewById<EditText>(R.id.workplacename) //알바 이름
            val wageday_EditText = findViewById<EditText>(R.id.wageday) // 월급 받는 날
            val wageamount_EditText = findViewById<EditText>(R.id.wageamount) // 시급
            val workstarttime_EditText = findViewById<EditText>(R.id.workstarttime) // 알바 출근 시각
            val workendtime_EditText = findViewById<EditText>(R.id.workendtime) // 알바 퇴근 시각
            val register_Button = findViewById<Button>(R.id.registerButton) // 등록
            val restSelectionButton = findViewById<Button>(R.id.restSelectionButton) // 주휴 수당
            val taxSelectionButton = findViewById<Button>(R.id.taxSelectionButton) // 세금 선택

            // 알바 출근 요일 선택 체크 박스, 주 며칠 출근하는 지 카운트
            val checkBoxes = listOf(
                findViewById<CheckBox>(R.id.monday),
                findViewById<CheckBox>(R.id.tuesday),
                findViewById<CheckBox>(R.id.wednesday),
                findViewById<CheckBox>(R.id.thursday),
                findViewById<CheckBox>(R.id.friday),
                findViewById<CheckBox>(R.id.saturday),
                findViewById<CheckBox>(R.id.sunday)
            )

            // 버튼 이벤트
            restSelectionButton.setOnClickListener { it.context.showRestDialog(restSelectionButton) } // 주휴 수당
            taxSelectionButton.setOnClickListener { it.context.showTaxDialog(taxSelectionButton) } // 세금 선택

            // 등록 버튼 클릭 시 Place 객체 생성하고, viewModel을 통해 데이터 추가
            register_Button.setOnClickListener {
                val place = createPlace(
                    work_placenameEditText,
                    wageday_EditText,
                    wageamount_EditText,
                    workstarttime_EditText,
                    workendtime_EditText,
                    checkBoxes,
                    restSelectionButton,
                    taxSelectionButton
                )
                viewModel.addPlace(place)
            }
        }
    }

    // 주휴수당 선택에 화면을 생성하고 보여주는 함수
    private fun Context.showRestDialog(button: Button) = AlertDialog.Builder(this).run {
        setTitle("주휴수당 선택")
        setItems(arrayOf("주휴수당 포함", "주휴수당 미포함")) { _, which ->
            button.text = if (which == 0) "주휴수당 포함" else "주휴수당 미포함"
        }
        create().show()
    }

    // 세금 선택에 대한 화면을 생성하고 보여주는 함수
    private fun Context.showTaxDialog(button: Button) = AlertDialog.Builder(this).run {
        setTitle("세금 선택")
        setItems(arrayOf("세금 적용 안함", "4대보험 적용 (9.32%)", "소득세 적용 (3.3%)")) { _, which ->
            button.text = when (which) {
                0 -> "세금 적용 안함"
                1 -> "4대보험 적용 (9.32%)"
                else -> "소득세 적용 (3.3%)"
            }
        }
        create().show()
    }


    private fun View.createPlace(
        placenameEditText: EditText,
        wagedayEditText: EditText,
        wageamountEditText: EditText,
        starttimeEditText: EditText,
        endtimeEditText: EditText,
        checkBoxes: List<CheckBox>,
        restButton: Button,
        taxButton: Button
    ): Place {
        val workPlaceName = placenameEditText.text.toString()
        val wageDay = wagedayEditText.text.toString()
        val wageAmount = wageamountEditText.text.toString()
        val workStartTime = starttimeEditText.text.toString()
        val workEndTime = endtimeEditText.text.toString()
        val dayCount = checkBoxes.count { it.isChecked }
        val dayCalendarBoolList = checkBoxes.map { if (it.isChecked) 1 else 0 }

        val salary = calculateSalary(
            wageAmount.toInt(),
            workEndTime.toInt() - workStartTime.toInt(),
            dayCount,
            restButton.text.toString(),
            taxButton.text.toString()
        )

        return Place(
            name = workPlaceName,
            wageday = wageDay,
            wageamount = wageAmount,
            starttime = workStartTime,
            endtime = workEndTime,
            daycount = dayCount,
            salary = salary,
            dayCalendarCheck = dayCalendarBoolList
        )
    }

    private fun calculateSalary(wage: Int, hours: Int, days: Int, rest: String, tax: String): Int {
        val monthlySalary = wage * hours * days * 4
        val restAmount = if (rest == "주휴수당 포함" && hours * days >= 15) {
            (hours.coerceAtMost(8) * days.coerceAtMost(5) * wage).toDouble()
        } else 0.0
        val taxRate = when (tax) {
            "4대보험 적용 (9.32%)" -> 0.0932
            "소득세 적용 (3.3%)" -> 0.033
            else -> 0.0
        }
        val taxAmount = (monthlySalary + restAmount) * taxRate
        return (monthlySalary + restAmount - taxAmount).toInt()
    }
}
