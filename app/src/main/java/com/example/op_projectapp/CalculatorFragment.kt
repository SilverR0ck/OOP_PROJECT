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


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [CalculatorFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
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
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment CalculatorFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            CalculatorFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}