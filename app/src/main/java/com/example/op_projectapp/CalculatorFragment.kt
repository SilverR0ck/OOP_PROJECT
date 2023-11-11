package com.example.op_projectapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Button
import android.widget.RadioGroup
import android.widget.TextView


class CalculatorFragment : Fragment() {


        override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {
        // Inflate the layout for this fragment
            val view = inflater.inflate(R.layout.fragment_calculator, container, false)

            val hourlyWage = view.findViewById<EditText>(R.id.hourlyWage)
            val dailyHours = view.findViewById<EditText>(R.id.dailyHours)
            val weeklyDays = view.findViewById<EditText>(R.id.weeklyDays)
            val calculateSalaryButton = view.findViewById<Button>(R.id.calculateSalaryButton)
            val radioGroupRest = view.findViewById<RadioGroup>(R.id.radioGroupRest)
            val radioGroupTax = view.findViewById<RadioGroup>(R.id.radioGroupTax)
            val resultText = view.findViewById<TextView>(R.id.resultText)

            calculateSalaryButton.setOnClickListener {

                resultText.text = ""
                val wage = hourlyWage.text.toString().toDoubleOrNull()
                val hours = dailyHours.text.toString().toDoubleOrNull()
                val days = weeklyDays.text.toString().toDoubleOrNull()

                if (wage != null && hours != null && days != null) {
                    val rest: Double
                    val tax: Double

                    when (radioGroupRest.checkedRadioButtonId) {
                        R.id.radioButtonInclude -> {
                            rest = if (hours * days >= 15) hours.coerceAtMost(8.0) * days.coerceAtMost(5.0) * wage else 0.0
                        }
                        else -> rest = 0.0
                    }

                    when (radioGroupTax.checkedRadioButtonId) {
                        R.id.radioButtonInsurance -> tax = 0.0932
                        R.id.radioButtonIncomeTax -> tax = 0.033
                        else -> tax = 0.0
                    }

                    val monthlySalary = wage * hours * days * 4
                    val taxAmount = (monthlySalary + rest) * tax
                    val takeHomePay = monthlySalary + rest - taxAmount

                    resultText.text = "예상 급여: $monthlySalary 원\n" +
                            "예상 주휴 수당: $rest 원\n" +
                            "예상 세금: $taxAmount 원\n" +
                            "예상 수령 금액: $takeHomePay 원"
                }
            }

            return view
    }
}