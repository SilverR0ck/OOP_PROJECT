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
import com.example.op_projectapp.databinding.FragmentAddworkBinding
import java.lang.Math.abs

class AddworkFragment : Fragment() {
    private var _binding: FragmentAddworkBinding? = null
    private val binding get() = _binding!!
    private val viewModel: PlaceViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddworkBinding.inflate(inflater, container, false)

        // 체크 박스 리스트
        val checkBoxes = listOf(
            binding.monday,
            binding.tuesday,
            binding.wednesday,
            binding.thursday,
            binding.friday,
            binding.saturday,
            binding.sunday
        )

        // AlertDialog 버튼 이벤트
        binding.restSelectionButton.setOnClickListener { it.context.showRestDialog(binding.restSelectionButton) } // 주휴 수당
        binding.taxSelectionButton.setOnClickListener { it.context.showTaxDialog(binding.taxSelectionButton) } // 세금 선택

        // 등록 버튼 클릭 시 Place 객체 생성하고, viewModel을 통해 데이터 추가
        binding.registerButton.setOnClickListener {
            var isValid = true
            var errorMessage = StringBuilder()

            if (binding.wageday.text.toString().isEmpty() || !TextUtils.isDigitsOnly(binding.wageday.text.toString())) {
                binding.wageday.error = "월급일은 숫자로 입력해주세요."
                isValid = false
            }

            if (binding.hourlyrate.text.toString().isEmpty() || !TextUtils.isDigitsOnly(binding.hourlyrate.text.toString())) {
                binding.hourlyrate.error = "시급은 숫자로 입력해주세요."
                isValid = false
            }

            if (binding.workstarttime.text.toString().isEmpty() || !TextUtils.isDigitsOnly(binding.workstarttime.text.toString())) {
                binding.workstarttime.error = "근무 시작시간은 숫자로 입력해주세요."
                isValid = false
            }

            if (binding.workendtime.text.toString().isEmpty() || !TextUtils.isDigitsOnly(binding.workendtime.text.toString())) {
                binding.workendtime.error = "근무 종료시간은 숫자로 입력해주세요."
                isValid = false
            }

            if (binding.workstartmonth.text.toString().isEmpty() || !TextUtils.isDigitsOnly(binding.workstartmonth.text.toString())) {
                binding.workstartmonth.error = "근무 시작 달은 숫자로 입력해주세요."
                isValid = false
            }

            if (binding.workstartday.text.toString().isEmpty() || !TextUtils.isDigitsOnly(binding.workstartday.text.toString())) {
                binding.workstartday.error = "근무 시작 일은 숫자로 입력해주세요."
                isValid = false
            }

            val startmonth = binding.workstartmonth.text.toString().toIntOrNull()
            val startday = binding.workstartday.text.toString().toIntOrNull()
            val starttime = binding.workstarttime.text.toString().toIntOrNull()
            val endtime = binding.workendtime.text.toString().toIntOrNull()

            if (binding.restSelectionButton.text == "주휴수당 선택") {
                errorMessage.append("주휴수당을 선택해주세요.\n")
                isValid = false
            }

            if (binding.taxSelectionButton.text == "세금 선택") {
                errorMessage.append("세금을 선택해주세요.\n")
                isValid = false
            }

            if (checkBoxes.none { it.isChecked }) {
                errorMessage.append("요일을 하나 이상 선택해주세요.\n")
                isValid = false
            }

            if (starttime == null || endtime == null || starttime !in 0..24 || endtime !in 0..24) {
                errorMessage.append("근무 시간은 0~24시간 형식으로 입력해주세요.")
                isValid = false
            }

            if (startmonth == null  || startmonth !in 1..12) {
                errorMessage.append("근무 월은 1~12달 형식으로 입력해주세요.")
                isValid = false
            }

            if (startday== null  || startday !in 1..31) {
                errorMessage.append("근무 일은 1~31일 형식으로 입력해주세요.")
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
                binding.workplacename,
                binding.workstartmonth,
                binding.workstartday,
                binding.wageday,
                binding.hourlyrate,
                binding.workstarttime,
                binding.workendtime,
                checkBoxes,
                binding.restSelectionButton,
                binding.taxSelectionButton
            )
            viewModel.addPlace(place)
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
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
    private fun createPlace(
        placenameEditText: EditText,
        workstartmonthEditText: EditText,
        workstartdayEditText: EditText,
        wagedayEditText: EditText,
        hourlyrateEditText: EditText,
        starttimeEditText: EditText,
        endtimeEditText: EditText,
        checkBoxes: List<CheckBox>,
        restButton: Button,
        taxButton: Button
    ): Place {
        val workPlaceName = placenameEditText.text.toString()
        val workStartMonth = workstartmonthEditText.text.toString()
        val workStartDay = workstartdayEditText.text.toString()
        val wageDay = wagedayEditText.text.toString()
        val hourlyRate = hourlyrateEditText.text.toString()
        val workStartTime = starttimeEditText.text.toString().toInt()
        val workEndTime = endtimeEditText.text.toString().toInt()
        val dayCount = checkBoxes.count { it.isChecked }
        val dayCalendarBoolList = checkBoxes.map { if (it.isChecked) 1 else 0 }
        val Hours = if (workStartTime > workEndTime) {
            abs(workStartTime - (workEndTime + 25))
        } else {
            workEndTime - workStartTime
        }
        val salary = SalaryCalculator.calculateSalary(
            hourlyRate,
            Hours,
            dayCount,
            restButton.text.toString(),
            taxButton.text.toString()
        )
        // 생성된 데이터를 바탕으로 Place 객체를 생성해서 반환

        return Place(
            name = workPlaceName,
            workstartmonth = workStartMonth,
            workstartday = workStartDay,
            wageday = wageDay,
            hourlyrate = hourlyRate,
            starttime = workStartTime.toString(),
            endtime = workEndTime.toString(),
            daycount = dayCount,
            salary = salary,
            dayCalendarCheck = dayCalendarBoolList
        )
    }
}

