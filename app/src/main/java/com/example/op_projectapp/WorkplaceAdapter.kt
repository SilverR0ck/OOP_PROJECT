package com.example.op_projectapp

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.op_projectapp.databinding.ListPlaceBinding

class WorkplaceAdapter(private val placeList: LiveData<List<Place>>) : RecyclerView.Adapter<WorkplaceAdapter.WorkplaceViewHolder>() {
    inner class WorkplaceViewHolder(val binding: ListPlaceBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WorkplaceViewHolder {
        val binding = ListPlaceBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return WorkplaceViewHolder(binding)
    }

    override fun onBindViewHolder(holder: WorkplaceViewHolder, position: Int) {
        val place = placeList.value?.get(position)
        holder.binding.txtName.text = place?.name
        holder.binding.txtWageday.text = place?.wageday
        holder.binding.txtSalary.text = place?.salary.toString()
        holder.binding.txtDaycount.text = place?.daycount.toString()

        holder.binding.btnChageWork.setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.action_homeFragment_to_changeworkFragment)
        }

    }

    override fun getItemCount() = placeList.value?.size ?: 0
}


