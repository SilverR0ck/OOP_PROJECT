package com.example.op_projectapp

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import androidx.fragment.app.Fragment

class ChangeworkFragment : Fragment() {
    private var name: String? = null
    private var wageday: String? = null
    private var salary: Int? = null
    private var daycount: Int? = null
    private var dayCalendarCheck: List<Int> = mutableListOf()
    private var starttime: String? = null
    private var endtime: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            name = it.getString("name")
            wageday = it.getString("wageday")
            salary = it.getInt("salary")
            daycount = it.getInt("daycount")
            dayCalendarCheck = it.getIntArray("dayCalendarCheck")?.toList() ?: mutableListOf()
            starttime = it.getString("starttime")
            endtime = it.getString("endtime")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_changework, container, false)
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

        view.findViewById<EditText>(R.id.workplacename).setText(name)
        view.findViewById<EditText>(R.id.wageday).setText(wageday)
        view.findViewById<EditText>(R.id.wageamount).setText(salary?.toString())

        view.findViewById<CheckBox>(R.id.monday).isChecked = dayCalendarCheck[0] != 0
        view.findViewById<CheckBox>(R.id.tuesday).isChecked = dayCalendarCheck[1] != 0
        view.findViewById<CheckBox>(R.id.wednesday).isChecked = dayCalendarCheck[2] != 0
        view.findViewById<CheckBox>(R.id.thursday).isChecked = dayCalendarCheck[3] != 0
        view.findViewById<CheckBox>(R.id.friday).isChecked = dayCalendarCheck[4] != 0
        view.findViewById<CheckBox>(R.id.saturday).isChecked = dayCalendarCheck[5] != 0
        view.findViewById<CheckBox>(R.id.sunday).isChecked = dayCalendarCheck[6] != 0

        view.findViewById<EditText>(R.id.workstarttime).setText(starttime)
        view.findViewById<EditText>(R.id.workendtime).setText(endtime)

        return view
    }

   /*ompanion object {
        fun newInstance(name: String, wageday: String, salary: Int, daycount: Int, dayCalendarCheck: IntArray, starttime: String, endtime: String) =
            ChangeworkFragment().apply {
                arguments = Bundle().apply {
                    putString("name", name)
                    putString("wageday", wageday)
                    putInt("salary", salary)
                    putInt("daycount", daycount)
                    putIntArray("dayCalendarCheck", dayCalendarCheck)
                    putString("starttime", starttime)
                    putString("endtime", endtime)
                }
            }
    }*/
}