package com.example.op_projectapp

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Button
import android.widget.RadioButton
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
            val resultText = view.findViewById<TextView>(R.id.resultText)
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


            calculateSalaryButton.setOnClickListener {
                resultText.text = ""
                val wage = hourlyWage.text.toString().toDoubleOrNull()
                val hours = dailyHours.text.toString().toDoubleOrNull()
                val days = weeklyDays.text.toString().toDoubleOrNull()

                if (wage != null && hours != null && days != null) {
                    val rest: Double
                    val tax: Double

                    when (restSelectionButton.text) {
                        "주휴수당 포함" -> {
                            rest = if (hours * days >= 15) hours.coerceAtMost(8.0) * days.coerceAtMost(5.0) * wage else 0.0
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
                    val takeHomePay = monthlySalary + rest - taxAmount

                    resultText.text = "예상 급여: $monthlySalary 원\n" + "예상 주휴 수당: $rest 원\n" + "예상 세금: $taxAmount 원\n" + "예상 수령 금액: $takeHomePay 원\n"
                }
            }


            return view
    }
}