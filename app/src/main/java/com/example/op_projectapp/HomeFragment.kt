package com.example.op_projectapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.op_projectapp.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    var binding: FragmentHomeBinding?= null
    val viewModel : PlaceViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater)
        // Inflate the layout for this fragment
        return binding?.root
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.run {
            btnAddwork.setOnClickListener {
                findNavController().navigate(R.id.action_homeFragment_to_addworkFragment)
            }
            val adapter = WorkplaceAdapter(viewModel.nameplace)
            recyclerInfo.layoutManager = LinearLayoutManager(activity)
            recyclerInfo.adapter = adapter

            viewModel.nameplace.observe(viewLifecycleOwner) {
                adapter.notifyDataSetChanged() // 데이터가 변경되면, RecyclerView를 갱신
            }
        }
        binding?.run {
            btnAddwork.setOnClickListener {
                findNavController().navigate(R.id.action_homeFragment_to_addworkFragment)
            }
        }
        binding?.run {
            btnAddwork.setOnClickListener {
                findNavController().navigate(R.id.action_homeFragment_to_addworkFragment)
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}