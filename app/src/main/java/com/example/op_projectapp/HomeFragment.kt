package com.example.op_projectapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.op_projectapp.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {
    val place = arrayOf(
        Place("스타벅스", 200000),
        Place("스터디카페", 100000),
        Place("투썸", 1000000)

    )

    var binding: FragmentHomeBinding?= null
    //private lateinit var viewAdapter: RecyclerView.Adapter<*>
    //private lateinit var viewManager: RecyclerView.LayoutManager


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

            recyclerInfo.apply {
                // use a linear layout manager
                layoutManager = LinearLayoutManager(activity)

                // specify an viewAdapter
                adapter = WorkplaceAdapter(place)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}