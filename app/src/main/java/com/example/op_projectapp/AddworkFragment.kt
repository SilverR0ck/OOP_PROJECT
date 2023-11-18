package com.example.op_projectapp

import android.app.AlertDialog
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

    //viewModel 객체 생성
    private val viewModel: PlaceViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_addwork, container, false)
        val work_placenameEditText = view.findViewById<EditText>(R.id.workplacename)
        val wageday_EditText = view.findViewById<EditText>(R.id.wageday)
        val wageamount_EditText = view.findViewById<EditText>(R.id.wageamount)
        val workstarttime_EditText = view.findViewById<EditText>(R.id.workstarttime)
        val workendtime_EditText = view.findViewById<EditText>(R.id.workendtime)
        val register_Button = view.findViewById<Button>(R.id.registerButton)
        val restSelectionButton = view.findViewById<Button>(R.id.restSelectionButton)
        val taxSelectionButton = view.findViewById<Button>(R.id.taxSelectionButton)

        restSelectionButton.setOnClickListener {
            val restOptions = arrayOf("주휴수당 포함", "주휴수당 미포함")
            val builder = AlertDialog.Builder(it.context)
            builder.setTitle("주휴수당 선택")
            builder.setItems(restOptions) { dialog, which ->
                restSelectionButton.text = restOptions[which]
            }
            val dialog = builder.create()
            dialog.show()
        }

        taxSelectionButton.setOnClickListener {
            val taxOptions = arrayOf("세금 적용 안함", "4대보험 적용 (9.32%)", "소득세 적용 (3.3%)")
            val builder = AlertDialog.Builder(it.context)
            builder.setTitle("세금 선택")
            builder.setItems(taxOptions) { dialog, which ->
                taxSelectionButton.text = taxOptions[which]
            }
            val dialog = builder.create()
            dialog.show()
        }


        register_Button.setOnClickListener {
            val workPlaceName = work_placenameEditText.text.toString() //알바장소
            val wageDay = wageday_EditText.text.toString() //월급받는 날짜
            val wageAmount = wageamount_EditText.text.toString() // 시급
            val workStartTime = workstarttime_EditText.text.toString() // 알바 시작 시간
            val workEndTime = workendtime_EditText.text.toString() // 알바 종료 시간
            // 체크 박스 개수에 따라 주 며칠 일하는지 카운트
            val mondayCheckBox = view.findViewById<CheckBox>(R.id.monday)
            val tuesdayCheckBox = view.findViewById<CheckBox>(R.id.tuesday)
            val wednesdayCheckBox = view.findViewById<CheckBox>(R.id.wednesday)
            val thursdayCheckBox = view.findViewById<CheckBox>(R.id.thursday)
            val fridayCheckBox = view.findViewById<CheckBox>(R.id.friday)
            val saturdayCheckBox = view.findViewById<CheckBox>(R.id.saturday)
            val sundayCheckBox = view.findViewById<CheckBox>(R.id.sunday)

            val dayCount = listOf(
                mondayCheckBox,
                tuesdayCheckBox,
                wednesdayCheckBox,
                thursdayCheckBox,
                fridayCheckBox,
                saturdayCheckBox,
                sundayCheckBox).count { it.isChecked } //주 며칠 출근 카운트

            val dayCalendarCheck = listOf(
                mondayCheckBox,
                tuesdayCheckBox,
                wednesdayCheckBox,
                thursdayCheckBox,
                fridayCheckBox,
                saturdayCheckBox,
                sundayCheckBox)
            val dayCalendarBoolList = dayCalendarCheck.map { if (it.isChecked) 1 else 0 }


            // 등록버튼 이벤트발생 시 위의 데이터에 따라 월급 계산
            var total_salary = 0.0
            val wage = wageAmount.toInt()
            val hours = workEndTime.toInt() - workStartTime.toInt()
            val days = dayCount // 일주일에 며칠 일하는지, 체크박스 개수

            if (wage != null && hours != null && days != null) {
                val rest: Double
                val tax: Double

                when (restSelectionButton.text) {
                    "주휴수당 포함" -> {
                        rest =
                            if (hours * days >= 15) (hours.coerceAtMost(8) * days.coerceAtMost(5) * wage).toDouble() else 0.0
                    }

                    else -> rest = 0.0
                }

                when (taxSelectionButton.text) {
                    "4대보험 적용 (9.32%)" -> tax = 0.0932
                    "소득세 적용 (3.3%)" -> tax = 0.033
                    else -> tax = 0.0
                }
                val monthlySalary = wage * hours * days * 4
                val taxAmount = (monthlySalary + rest) * tax
                total_salary = monthlySalary + rest - taxAmount //최종 예상 수령 금액
            }
            val place = Place(
                name = workPlaceName,
                wageday = wageDay,
                wageamount = wageAmount,
                starttime = workStartTime,
                endtime = workEndTime,
                daycount = dayCount,
                salary = total_salary.toInt(),
                dayCalendarCheck = dayCalendarBoolList
            )
            viewModel.addPlace(place)
        }
        return view
    }
}