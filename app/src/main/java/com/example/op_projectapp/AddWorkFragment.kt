package com.example.op_projectapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import com.google.firebase.Firebase
import com.google.firebase.database.database


class AddWorkFragment : Fragment() {

    // 데이터베이스 참조
    private val database = Firebase.database
    private val WorkRef = database.getReference("places")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_addwork, container, false)

        val work_placenameEditText = view.findViewById<EditText>(R.id.workplacename)
        val register_Button = view.findViewById<EditText>(R.id.registerButton)


        register_Button.setOnClickListener{
            val workPlaceName = work_placenameEditText.text.toString()
            //Place객체에 근무지 이름, 월급, 시간 등 데이터를 넘겨서 객체 생성
            val add_Work = Place(workPlaceName)
            // 생성된 객체를 데이터 베이스에 저장
            WorkRef.setValue(add_Work)

        }


        // Inflate the layout for this fragment
        return view
    }

}
