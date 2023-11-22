package com.example.op_projectapp


import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import androidx.fragment.app.Fragment
import com.example.op_projectapp.repository.PlaceRepository
import com.google.firebase.database.FirebaseDatabase

// Place 객체의 속성을 변경하는 fragment
class ChangeworkFragment : Fragment() {
    private var name: String? = null
    private var hourlyrate: String? = null
    private var wageday: String? = null
    private var daycount: Int? = null
    private var dayCalendarCheck: List<Int> = mutableListOf()
    private var starttime: String? = null
    private var endtime: String? = null
    private val repository = PlaceRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            name = it.getString("name")
            wageday = it.getString("wageday")
            daycount = it.getInt("daycount")
            dayCalendarCheck = it.getIntArray("dayCalendarCheck")?.toList() ?: mutableListOf()
            starttime = it.getString("starttime")
            endtime = it.getString("endtime")
            hourlyrate = it.getString("hourlyrate").toString()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_changework, container, false)

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

        view.findViewById<EditText>(R.id.workplacename).setText(name)
        view.findViewById<EditText>(R.id.wageday).setText(wageday)
        view.findViewById<EditText>(R.id.hourlyrate).setText(hourlyrate)
        view.findViewById<EditText>(R.id.workstarttime).setText(starttime)
        view.findViewById<EditText>(R.id.workendtime).setText(endtime)

        checkBoxes[0].isChecked = dayCalendarCheck[0] != 0
        checkBoxes[1].isChecked = dayCalendarCheck[1] != 0
        checkBoxes[2].isChecked = dayCalendarCheck[2] != 0
        checkBoxes[3].isChecked = dayCalendarCheck[3] != 0
        checkBoxes[4].isChecked = dayCalendarCheck[4] != 0
        checkBoxes[5].isChecked = dayCalendarCheck[5] != 0
        checkBoxes[6].isChecked = dayCalendarCheck[6] != 0

        view.findViewById<Button>(R.id.updateButton).setOnClickListener {
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
            // 해당 name의 기존객체를 찾아 삭제하고 새로운 객체를 만들어 업데이트
            name?.let { repository.deletePlace(it) }
            repository.updatePlace(place)
        }

        return view
    }

    // 수정된 데이터를 바탕으로 업데이트된 객체를 생성하는 함수
    private fun createPlace(
        placenameEditText: EditText,
        wagedayEditText: EditText,
        hourlyrateEditText: EditText,
        starttimeEditText: EditText,
        endtimeEditText: EditText,
        checkBoxes: List<CheckBox>,
        restButton: Button, taxButton: Button
    ): Place {
        val newHourlyrate = hourlyrateEditText.text.toString()
        val newWorkStartTime = starttimeEditText.text.toString()
        val newWorkEndTime = endtimeEditText.text.toString()
        val newDayCount = checkBoxes.count { it.isChecked }
        val newSalary = SalaryCalculator.calculateSalary(newHourlyrate, newWorkStartTime, newWorkEndTime, newDayCount, restButton.text.toString(), taxButton.text.toString())

        // 객체의 데이터를 반환하여 생성
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

    // 주휴수당, 세금을 선택하는 창
    private fun Context.showChoiceDialog(title: String, items: Array<String>, button: Button) {
        AlertDialog.Builder(this).apply {
            setTitle(title)
            setItems(items) { _, which -> button.text = items[which] }
            create().show()
        }
    }
}