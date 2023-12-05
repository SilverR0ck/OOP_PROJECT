package com.example.op_projectapp


import android.app.AlertDialog
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.op_projectapp.databinding.FragmentChangeworkBinding
import com.example.op_projectapp.repository.PlaceRepository
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

import java.lang.Math.abs
class ChangeworkFragment : Fragment() {
    private var _binding: FragmentChangeworkBinding? = null
    private val binding get() = _binding!!
    private var name: String? = null
    private var workstartmonth: String? = null
    private var workstartday: String? = null
    private var hourlyrate: String? = null
    private var wageday: String? = null
    private var daycount: Int? = null
    private var dayCalendarCheck: List<Int> = mutableListOf()
    private var starttime: String? = null
    private var endtime: String? = null
    private var salary: MutableList<Int>? = null


    private val repository = PlaceRepository() // PlaceRepository 인스턴스 생성

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            name = it.getString("name")
            workstartmonth = it.getString("workstartmonth")
            workstartday = it.getString("workstartday")
            wageday = it.getString("wageday")
            daycount = it.getInt("daycount")
            dayCalendarCheck = it.getIntArray("dayCalendarCheck")?.toList() ?: mutableListOf()
            starttime = it.getString("starttime")
            endtime = it.getString("endtime")
            hourlyrate = it.getString("hourlyrate").toString()
            repository.WorkRef.child(name ?: "").addListenerForSingleValueEvent(object :
                ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    salary = snapshot.getValue(Place::class.java)?.salary?.toMutableList()
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("ChangeworkFragment", "Failed to load place", error.toException())
                }
            })
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentChangeworkBinding.inflate(inflater, container, false)

        binding.btnDel.setOnClickListener {
            name?.let { repository.deletePlace(it) }
            findNavController().navigate(R.id.action_changeworkFragment_to_homeFragment)
        }

        binding.restSelectionButton.setOnClickListener {
            val restOptions = arrayOf("주휴수당 포함", "주휴수당 미포함")
            val builder = AlertDialog.Builder(it.context)
            builder.setTitle("주휴수당 선택")
            builder.setItems(restOptions) { dialog, which ->
                binding.restSelectionButton.text = restOptions[which]
            }
            val dialog = builder.create()
            dialog.show()
        }


        binding.taxSelectionButton.setOnClickListener {
            val taxOptions = arrayOf("세금 적용 안함", "4대보험 적용 (9.32%)", "소득세 적용 (3.3%)")
            val builder = AlertDialog.Builder(it.context)
            builder.setTitle("세금 선택")
            builder.setItems(taxOptions) { dialog, which ->
                binding.taxSelectionButton.text = taxOptions[which]
            }
            val dialog = builder.create()
            dialog.show()
        }

        binding.workplacename.setText(name)
        binding.workstartmonth.setText(workstartmonth)
        binding.workstartday.setText(workstartday)
        binding.wageday.setText(wageday)
        binding.hourlyrate.setText(hourlyrate)
        binding.workstarttime.setText(starttime)
        binding.workendtime.setText(endtime)

        binding.monday.isChecked = dayCalendarCheck[0] != 0
        binding.tuesday.isChecked = dayCalendarCheck[1] != 0
        binding.wednesday.isChecked = dayCalendarCheck[2] != 0
        binding.thursday.isChecked = dayCalendarCheck[3] != 0
        binding.friday.isChecked = dayCalendarCheck[4] != 0
        binding.saturday.isChecked = dayCalendarCheck[5] != 0
        binding.sunday.isChecked = dayCalendarCheck[6] != 0
        binding.updateButton.setOnClickListener {
            val newName = binding.workplacename.text.toString()
            val newWorkStartMonth = binding.workstartmonth.text.toString()
            val newWorkStartDay = binding.workstartday.text.toString()
            val newWageday = binding.wageday.text.toString()
            val newHourlyrate = binding.hourlyrate.text.toString()
            val newWorkStartTime = binding.workstarttime.text.toString().toInt()
            var newWorkEndTime = binding.workendtime.text.toString().toInt()

            // '수정' 버튼 클릭 이벤트
            val newDayCalendarCheck = listOf<Int>(
                if (binding.monday.isChecked) 1 else 0,
                if (binding.tuesday.isChecked) 1 else 0,
                if (binding.wednesday.isChecked) 1 else 0,
                if (binding.thursday.isChecked) 1 else 0,
                if (binding.friday.isChecked) 1 else 0,
                if (binding.saturday.isChecked) 1 else 0,
                if (binding.sunday.isChecked) 1 else 0,
            )


            var isValid = true
            var errorMessage = StringBuilder()

            if (newWorkStartMonth.isEmpty() || !TextUtils.isDigitsOnly(newWorkStartMonth)) {
                binding.workstartmonth.error = "월급일은 숫자로 입력해주세요."
                isValid = false
            }

            if (newWorkStartDay.isEmpty() || !TextUtils.isDigitsOnly(newWorkStartDay)) {
                binding.workstartday.error = "월급일은 숫자로 입력해주세요."
                isValid = false
            }

            if (newWageday.isEmpty() || !TextUtils.isDigitsOnly(newWageday)) {
                binding.wageday.error = "월급일은 숫자로 입력해주세요."
                isValid = false
            }

            if (newHourlyrate.isEmpty() || !TextUtils.isDigitsOnly(newHourlyrate)) {
                binding.hourlyrate.error = "시급은 숫자로 입력해주세요."
                isValid = false
            }

            if (newWorkStartTime.toString().isEmpty() || !TextUtils.isDigitsOnly(newWorkStartTime.toString())) {
                binding.workstarttime.error = "근무 시작시간은 숫자로 입력해주세요."
                isValid = false
            }

            if (newWorkEndTime.toString().isEmpty() || !TextUtils.isDigitsOnly(newWorkEndTime.toString())) {
                binding.workendtime.error = "근무 종료시간은 숫자로 입력해주세요."
                isValid = false
            }

            // 근무 월일 및 출퇴근시간 널이 아니라면, 근무달은 1~12월까지, 근무일은 1~31일까지 입력가능, 출퇴근시간은 0~24시간까지 입력가능 범위 설정
            val startmonth = newWorkStartMonth.toIntOrNull()
            val startday = newWorkStartDay.toIntOrNull()
            val starttime = newWorkStartTime.toString().toIntOrNull()
            val endtime = newWorkEndTime.toString().toIntOrNull()

            if (binding.restSelectionButton.text == "주휴수당 선택") {
                errorMessage.append("주휴수당을 선택해주세요.\n")
                isValid = false
            }

            if (binding.taxSelectionButton.text == "세금 선택") {
                errorMessage.append("세금을 선택해주세요.\n")
                isValid = false
            }

            if (newDayCalendarCheck.none { it == 1 }) {
                errorMessage.append("요일을 하나 이상 선택해주세요.\n")
                isValid = false
            }

            if (starttime == null || endtime == null || starttime !in 0..24 || endtime !in 0..24) {
                errorMessage.append("근무 시간은 0~24시간 형식으로 입력해주세요.")
                isValid = false
            }

            if (startmonth == null || startday == null || startmonth !in 1..12 || startday !in 1..31) {
                errorMessage.append("근무 시간은 0~24시간 형식으로 입력해주세요.")
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

            val newDayCount = newDayCalendarCheck.filter { it == 1 }.size // 새로운 daycount 계산
            val newHours = if (newWorkStartTime > newWorkEndTime) {
                abs(newWorkStartTime - (newWorkEndTime + 24))
            } else {
                newWorkEndTime - newWorkStartTime
            }
            val newRest = binding.restSelectionButton.text.toString()
            val newTax = binding.taxSelectionButton.text.toString()
            val newSalaryPerMonth = SalaryCalculator.calculateSalary(
                newHourlyrate,
                newHours,
                newDayCount,
                newRest,
                newTax
            )
            val newSalary = MutableList(12) { 0 } // 모든 월에 대해 기본 월급을 0으로 설정
            val startMonthIndex = newWorkStartMonth.toInt() - 1
            salary?.let {
                it[startMonthIndex] = newSalaryPerMonth
            }
            // 기존 노드 삭제

            name?.let { repository.deletePlace(it) }



            // 새 노드 생성
            repository.updatePlace(
                Place(
                    name = newName,
                    workstartmonth = newWorkStartMonth,
                    workstartday = newWorkStartDay,
                    wageday = newWageday,
                    starttime = newWorkStartTime.toString(),
                    endtime = newWorkEndTime.toString(),
                    dayCalendarCheck = newDayCalendarCheck,
                    daycount = newDayCount,
                    hourlyrate = newHourlyrate,
                    salary = salary ?: newSalary
                )
            )
            findNavController().navigate(R.id.action_changeworkFragment_to_homeFragment)
        }
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}