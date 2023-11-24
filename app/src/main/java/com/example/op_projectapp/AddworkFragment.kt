package com.example.op_projectapp

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import androidx.fragment.app.viewModels
import com.google.firebase.database.FirebaseDatabase
import android.text.TextUtils
import android.widget.Toast

class AddworkFragment : Fragment() {

    // viewModel PlaceViewModel 인스턴스 생성
    private val viewModel: PlaceViewModel by viewModels()
    val database = FirebaseDatabase.getInstance().reference
    // AddworkFragment의 뷰를 생성 및 초기화
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // AddWork뷰 inflate하고 뷰 관련 설정
        return inflater.inflate(R.layout.fragment_addwork, container, false).apply {
            val work_placenameEditText = findViewById<EditText>(R.id.workplacename) //알바 이름
            val wageday_EditText = findViewById<EditText>(R.id.wageday) // 월급 받는 날
            val hourlyrate_EditText = findViewById<EditText>(R.id.hourlyrate) // 시급
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

            // AlertDialog 버튼 이벤트
            restSelectionButton.setOnClickListener { it.context.showRestDialog(restSelectionButton) } // 주휴 수당
            taxSelectionButton.setOnClickListener { it.context.showTaxDialog(taxSelectionButton) } // 세금 선택

            // 등록 버튼 클릭 시 Place 객체 생성하고, viewModel을 통해 데이터 추가
            register_Button.setOnClickListener {
                var isValid = true
                var errorMessage = StringBuilder()

                if (wageday_EditText.text.toString().isEmpty() || !TextUtils.isDigitsOnly(wageday_EditText.text.toString())) {
                    wageday_EditText.error = "월급일은 숫자로 입력해주세요."
                    isValid = false
                }

                if (hourlyrate_EditText.text.toString().isEmpty() || !TextUtils.isDigitsOnly(hourlyrate_EditText.text.toString())) {
                    hourlyrate_EditText.error = "시급은 숫자로 입력해주세요."
                    isValid = false
                }

                if (workstarttime_EditText.text.toString().isEmpty() || !TextUtils.isDigitsOnly(workstarttime_EditText.text.toString())) {
                    workstarttime_EditText.error = "근무 시작시간은 숫자로 입력해주세요."
                    isValid = false
                }

                if (workendtime_EditText.text.toString().isEmpty() || !TextUtils.isDigitsOnly(workendtime_EditText.text.toString())) {
                    workendtime_EditText.error = "근무 종료시간은 숫자로 입력해주세요."
                    isValid = false
                }

                val start = workstarttime_EditText.text.toString().toIntOrNull()
                val end = workendtime_EditText.text.toString().toIntOrNull()

                if (restSelectionButton.text == "주휴수당 선택") {
                    errorMessage.append("주휴수당을 선택해주세요.\n")
                    isValid = false
                }

                if (taxSelectionButton.text == "세금 선택") {
                    errorMessage.append("세금을 선택해주세요.\n")
                    isValid = false
                }

                if (checkBoxes.none { it.isChecked }) {
                    errorMessage.append("요일을 하나 이상 선택해주세요.\n")
                    isValid = false
                }

                if (start == null || end == null || start !in 0..24 || end !in 0..24) {
                    errorMessage.append("근무 시간은 0~24시간 형식으로 입력해주세요.")
                    isValid = false
                }

                if (start != null && end != null && end <= start) {
                    errorMessage.append("근무 종료 시간은 근무 시작 시간보다 커야 합니다.")
                    isValid = false
                }


                if (!isValid) {
                    AlertDialog.Builder(context).apply {
                        setTitle("입력 오류")
                        setMessage(errorMessage.toString())
                        setPositiveButton("확인", null)
                        create()
                        show()
                    }
                    return@setOnClickListener
                }

                val place = createPlace(
                    work_placenameEditText,
                    wageday_EditText,
                    hourlyrate_EditText,
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

    // 주휴수당 선택에 화면을 생성 함수
    private fun Context.showRestDialog(button: Button) = AlertDialog.Builder(this).run {
        setTitle("주휴수당 선택")
        setItems(arrayOf("주휴수당 포함", "주휴수당 미포함")) { _, which ->
            button.text = if (which == 0) "주휴수당 포함" else "주휴수당 미포함"
        }
        create().show()
    }

    // 세금 선택에 대한 화면을 생성 함수
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


    // 사용자로부터 데이터를 입력받고 이를 바탕으로 Place 객체를 생성하는 함수
    private fun View.createPlace(
        placenameEditText: EditText,
        wagedayEditText: EditText,
        hourlyrateEditText: EditText,
        starttimeEditText: EditText,
        endtimeEditText: EditText,
        checkBoxes: List<CheckBox>,
        restButton: Button,
        taxButton: Button
    ): Place {
        val workPlaceName = placenameEditText.text.toString()
        val wageDay = wagedayEditText.text.toString()
        val hourlyRate = hourlyrateEditText.text.toString()
        val workStartTime = starttimeEditText.text.toString()
        val workEndTime = endtimeEditText.text.toString()
        val dayCount = checkBoxes.count { it.isChecked }
        val dayCalendarBoolList = checkBoxes.map { if (it.isChecked) 1 else 0 }
        val salary = calculateSalary(
            hourlyRate,
            workEndTime.toInt() - workStartTime.toInt(),
            dayCount,
            restButton.text.toString(),
            taxButton.text.toString()
        )
        // 생성된 데이터를 바탕으로 Place 객체를 생성해서 반환
        return Place(
            name = workPlaceName,
            wageday = wageDay,
            hourlyrate = hourlyRate.toString(),
            starttime = workStartTime,
            endtime = workEndTime,
            daycount = dayCount,
            salary = salary,
            dayCalendarCheck = dayCalendarBoolList
        )
    }

    // 월급 계산 함수
    fun calculateSalary(wage: String, hours: Int, days: Int, rest: String, tax: String): Int {
        val wage = wage.toInt()
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

