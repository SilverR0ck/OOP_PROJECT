package com.example.op_projectapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.op_projectapp.databinding.FragmentHomeBinding
import com.example.op_projectapp.viewModel.PlaceViewModel

class HomeFragment : Fragment() {

    var binding: FragmentHomeBinding?= null
    val viewModel: PlaceViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.run {
            // 추가 버튼 클릭 시 근무지 추가화면으로 이동
            btnAddwork.setOnClickListener {
                findNavController().navigate(R.id.action_homeFragment_to_addworkFragment)
            }
            // 리사이클러뷰에 데이터 어댑터 설정
            val adapter = WorkplaceAdapter(viewModel.places)
            recyclerInfo.layoutManager = LinearLayoutManager(activity)
            recyclerInfo.adapter = adapter

            // Live Data로 데이터 변경 감지 및 리사이클러뷰 갱신
            viewModel.places.observe(viewLifecycleOwner) {
                adapter.notifyDataSetChanged()
            }
        }
    }

    /// 메모리 바인딩 해제
    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}