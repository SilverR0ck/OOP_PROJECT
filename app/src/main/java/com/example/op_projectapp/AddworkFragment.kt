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



class AddworkFragment : Fragment() {
    private val viewModel: PlaceViewModel by viewModels()
    val database = FirebaseDatabase.getInstance().reference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_addwork, container, false)

        val checkBoxes = listOf(
            view.findViewById<CheckBox>(R.id.monday),
            view.findViewById<CheckBox>(R.id.tuesday),
            view.findViewById<CheckBox>(R.id.wednesday),
            view.findViewById<CheckBox>(R.id.thursday),
            view.findViewById<CheckBox>(R.id.friday),
            view.findViewById<CheckBox>(R.id.saturday),
            view.findViewById<CheckBox>(R.id.sunday)
        )

        val restButton = view.findViewById<Button>(R.id.restSelectionButton)
        val taxButton = view.findViewById<Button>(R.id.taxSelectionButton)

        restButton.setOnClickListener {it.context.showChoiceDialog("주휴수당 선택", arrayOf("주휴수당 포함", "주휴수당 미포함"), it as Button) }
        taxButton.setOnClickListener {it.context.showChoiceDialog("세금 선택", arrayOf("세금 적용 안함", "4대보험 적용 (9.32%)", "소득세 적용 (3.3%)"), it as Button) }

        view.findViewById<Button>(R.id.registerButton).setOnClickListener {
            val place = createPlace(
                view.findViewById(R.id.workplacename),
                view.findViewById(R.id.wageday),
                view.findViewById(R.id.hourlyrate),
                view.findViewById(R.id.workstarttime),
                view.findViewById(R.id.workendtime),
                checkBoxes,
                restButton,
                taxButton
            )
            viewModel.addPlace(place)
        }

        return view
    }

    // 사용자의 알바정보를 입력받아 객체를 생성하는 함수
    private fun createPlace(
        placenameEditText: EditText,
        wagedayEditText: EditText,
        hourlyrateEditText: EditText,
        starttimeEditText: EditText,
        endtimeEditText: EditText,
        checkBoxes: List<CheckBox>,
        restButton: Button, taxButton: Button
    ): Place {
        // 월급을 계산할 때 필요한 요소 입력받아 변수 저장
        val newHourlyrate = hourlyrateEditText.text.toString()
        val newWorkStartTime = starttimeEditText.text.toString()
        val newWorkEndTime = endtimeEditText.text.toString()
        val newDayCount = checkBoxes.count { it.isChecked }
        val newSalary = SalaryCalculator.calculateSalary(newHourlyrate, newWorkStartTime, newWorkEndTime, newDayCount, restButton.text.toString(), taxButton.text.toString())


        return Place(
            name = placenameEditText.text.toString(),
            wageday = wagedayEditText.text.toString(),
            hourlyrate = hourlyrateEditText.text.toString(),
            starttime = starttimeEditText.text.toString(),
            endtime = endtimeEditText.text.toString(),
            daycount = checkBoxes.count { it.isChecked },
            dayCalendarCheck = checkBoxes.map { if (it.isChecked) 1 else 0 },
            salary = newSalary
        )
    }


    private fun Context.showChoiceDialog(title: String, items: Array<String>, button: Button) {
        AlertDialog.Builder(this).apply {
            setTitle(title)
            setItems(items) { _, which -> button.text = items[which] }
            create().show()
        }
    }

}
object SalaryCalculator {
    fun calculateSalary(wage: String, start: String, end: String, days: Int, rest: String, tax: String): Int {
        val hours = end.toInt() - start.toInt()
        val monthlySalary = wage.toInt() * hours * days * 4
        val restAmount = if (rest == "주휴수당 포함" && hours * days >= 15) (hours.coerceAtMost(8) * days.coerceAtMost(5) * wage.toInt()).toDouble() else 0.0
        val taxRate = when (tax) {
            "4대보험 적용 (9.32%)" -> 0.0932
            "소득세 적용 (3.3%)" -> 0.033
            else -> 0.0
        }
        val taxAmount = (monthlySalary + restAmount) * taxRate
        return (monthlySalary + restAmount - taxAmount).toInt()
    }

}