package com.example.op_projectapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.example.op_projectapp.Model.Place
import com.example.op_projectapp.databinding.ListPlaceBinding
import java.text.NumberFormat
import java.util.Calendar


// ListView의 각 View와 실제 데이터 매칭 역할
class WorkplaceAdapter(private val placeList: LiveData<List<Place>>) : RecyclerView.Adapter<WorkplaceAdapter.WorkplaceViewHolder>() {
    // 리사이클러뷰의 각 근무지 객체를 나타 내는 뷰 홀더
    class WorkplaceViewHolder(val binding: ListPlaceBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WorkplaceViewHolder {
       // 뷰홀더 생성, 바인딩 객체 초기화
        val binding = ListPlaceBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return WorkplaceViewHolder(binding)
    }

    override fun onBindViewHolder(holder: WorkplaceViewHolder, position: Int) {
        // 근무지 객체의 현재 달의 데이터를 가져 와서 뷰에 바인딩
        val currentMonth = Calendar.getInstance().get(Calendar.MONTH)
        val place = placeList.value?.get(position)
        val formattedSalary = place?.salary?.get(currentMonth)?.let { NumberFormat.getNumberInstance().format(it) } ?: "0"

        // 바인딩된 근무지 객체 사용
        holder.binding.apply {
            txtName.text = place?.name
            txtWageday.text = place?.wageday
            txtSalary.text = formattedSalary
            txtDaycount.text = place?.daycount.toString()

            // 수정 버튼 클릭 이벤트
            btnChageWork.setOnClickListener {
                // 근무지 수정 화면으로 데이터 전달을 위해 번들 생성
                val bundle = Bundle().apply {
                    putString("name", place?.name)
                    putString("workstartmonth", place?.workstartmonth)
                    putString("workstartday", place?.workstartday)
                    putString("wageday", place?.wageday)
                    putString("hourlyrate", place?.hourlyrate)
                    putInt("salary", place?.salary?.get(currentMonth) ?: 0)
                    putInt("daycount", place?.daycount ?: 0)
                    putIntArray("dayCalendarCheck", place?.dayCalendarCheck?.toIntArray() ?: IntArray(7)
                    )
                    putString("starttime", place?.starttime)
                    putString("endtime", place?.endtime)
                }
                // 리사이클러 뷰에서 객체의 수정 버튼 클릭시 수정 화면으로 네비게이션
                Navigation.findNavController(it).navigate(R.id.action_homeFragment_to_changeworkFragment, bundle)
            }
        }
    }
    // 리사이클뷰에 표시할 항목의 개수를 반환하는 메서드, 비어있을 때 문제 방지
    override fun getItemCount() = placeList.value?.size ?: 0
}
//리사이클러뷰는 자체적으로 뷰 홀더의 재사용 메커니즘을 가지고 있어, 뷰홀더와 그에 연결된 바인딩 객체가 자동으로 메모리에서 해제됨