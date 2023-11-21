package com.example.op_projectapp


import android.app.AlertDialog
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

class ChangeworkFragment : Fragment() {
    private var name: String? = null
    private var hourlyrate: String? = null
    private var wageday: String? = null
    private var daycount: Int? = null
    private var dayCalendarCheck: List<Int> = mutableListOf()
    private var starttime: String? = null
    private var endtime: String? = null

    private val repository = PlaceRepository() // PlaceRepository 인스턴스 생성
    val database = FirebaseDatabase.getInstance().reference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            name = it.getString("name")
            wageday = it.getString("wageday")
            daycount = it.getInt("daycount")
            dayCalendarCheck = it.getIntArray("dayCalendarCheck")?.toList() ?: mutableListOf()
            starttime = it.getString("starttime")
            endtime = it.getString("endtime")
            hourlyrate = it.getString("hourlyrate")
        }
        Log.d("wageamountvaule", "Deleting place with key: $hourlyrate")
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_changework, container, false)
        val restSelectionButton = view.findViewById<Button>(R.id.restSelectionButton)
        val taxSelectionButton = view.findViewById<Button>(R.id.taxSelectionButton)
        val updateButton = view.findViewById<Button>(R.id.updateButton)

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

        view.findViewById<EditText>(R.id.workplacename).setText(name)
        view.findViewById<EditText>(R.id.wageday).setText(wageday)
        view.findViewById<EditText>(R.id.hourlyrate).setText(hourlyrate)
        view.findViewById<EditText>(R.id.workstarttime).setText(starttime)
        view.findViewById<EditText>(R.id.workendtime).setText(endtime)

        view.findViewById<CheckBox>(R.id.monday).isChecked = dayCalendarCheck[0] != 0
        view.findViewById<CheckBox>(R.id.tuesday).isChecked = dayCalendarCheck[1] != 0
        view.findViewById<CheckBox>(R.id.wednesday).isChecked = dayCalendarCheck[2] != 0
        view.findViewById<CheckBox>(R.id.thursday).isChecked = dayCalendarCheck[3] != 0
        view.findViewById<CheckBox>(R.id.friday).isChecked = dayCalendarCheck[4] != 0
        view.findViewById<CheckBox>(R.id.saturday).isChecked = dayCalendarCheck[5] != 0
        view.findViewById<CheckBox>(R.id.sunday).isChecked = dayCalendarCheck[6] != 0

        // '수정' 버튼 클릭 이벤트
        updateButton.setOnClickListener {
           // val newKey = database.push().key
            val newName = view.findViewById<EditText>(R.id.workplacename).text.toString()
            val newWageday = view.findViewById<EditText>(R.id.wageday).text.toString()
            val newStarttime = view.findViewById<EditText>(R.id.workstarttime).text.toString()
            val newEndtime = view.findViewById<EditText>(R.id.workendtime).text.toString()
            val newHourlyrate = view.findViewById<EditText>(R.id.hourlyrate).text.toString()
            val newWorkStartTime = view.findViewById<EditText>(R.id.workstarttime).text.toString()
            val newWorkEndTime = view.findViewById<EditText>(R.id.workendtime).text.toString()
            val newRestSelectionButton = view.findViewById<Button>(R.id.restSelectionButton)
            val newTaxSelectionButton = view.findViewById<Button>(R.id.taxSelectionButton)

            val newDayCalendarCheck = listOf<Int>(
                if (view.findViewById<CheckBox>(R.id.monday).isChecked) 1 else 0,
                if (view.findViewById<CheckBox>(R.id.tuesday).isChecked) 1 else 0,
                if (view.findViewById<CheckBox>(R.id.wednesday).isChecked) 1 else 0,
                if (view.findViewById<CheckBox>(R.id.thursday).isChecked) 1 else 0,
                if (view.findViewById<CheckBox>(R.id.friday).isChecked) 1 else 0,
                if (view.findViewById<CheckBox>(R.id.saturday).isChecked) 1 else 0,
                if (view.findViewById<CheckBox>(R.id.sunday).isChecked) 1 else 0,
            )
            val newDayCount = newDayCalendarCheck.filter { it == 1 }.size // 새로운 daycount 계산
            val newSalary = calculateSalary(
                newHourlyrate.toInt(),
                newWorkEndTime.toInt() - newWorkStartTime.toInt(),
                newDayCount,
                newRestSelectionButton.text.toString(),
                newTaxSelectionButton.text.toString()
            )

            // 기존 노드 삭제

            name?.let { repository.deletePlace(it) }

            // 새 노드 생성
            repository.updatePlace(
                Place(
                    //key = newKey.toString(),
                    name = newName,
                    wageday = newWageday,
                    starttime = newStarttime,
                    endtime = newEndtime,
                    dayCalendarCheck = newDayCalendarCheck,
                    daycount = newDayCount,
                    hourlyrate = newHourlyrate,
                    salary = newSalary
                )
            )
        }
        return view
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