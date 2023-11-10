package com.example.op_projectapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.op_projectapp.databinding.ListPlaceBinding

class WorkplaceAdapter(val place: Array<Place>) : RecyclerView.Adapter<WorkplaceAdapter.Holder>(){


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding = ListPlaceBinding.inflate(LayoutInflater.from(parent.context))
        return Holder(binding)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(place[position])
    }

    //렌더링 개수
    override fun getItemCount() = place.size

    class Holder(private val binding : ListPlaceBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(place: Place){
            binding.txtName.text = place.name
            binding.txtSalary.text = place.salary.toString()
        }
    }
}

