package com.example.op_projectapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
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
        val salary_EditText = view.findViewById<EditText>(R.id.wageday)
        val register_Button = view.findViewById<Button>(R.id.registerButton)



        register_Button.setOnClickListener{
            val workPlaceName = work_placenameEditText.text.toString()
            val salary = salary_EditText.text.toString()
            //Place객체에 근무지 이름, 월급, 시간 등 데이터를 넘겨서 객체 생성


            val place = Place(workPlaceName, salary)
            viewModel.addPlace(place)

        }


        // Inflate the layout for this fragment
        return view
    }
}