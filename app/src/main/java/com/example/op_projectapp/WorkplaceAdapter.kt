package com.example.op_projectapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.example.op_projectapp.databinding.ListPlaceBinding
import java.text.NumberFormat
import java.util.Calendar


// 어댑터파일은 주로 데이터와 사용자 인터페이스를 연결하는 역할
// HomeFragment에서 Place객체의 리스트를 리사이클뷰로 보여주는 어댑터
class WorkplaceAdapter(private val placeList: LiveData<List<Place>>) : RecyclerView.Adapter<WorkplaceAdapter.WorkplaceViewHolder>() {
    // viewHolder는 리사이클뷰가 화면에 보여주는 각 항목을 대표하는 객체
    inner class WorkplaceViewHolder(val binding: ListPlaceBinding) : RecyclerView.ViewHolder(binding.root)

    //viewHolder 객체 생성
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WorkplaceViewHolder {
        val binding = ListPlaceBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return WorkplaceViewHolder(binding)
    }

    // viewHolder 객체를 데이터와 연결하는 메서드
    override fun onBindViewHolder(holder: WorkplaceViewHolder, position: Int) {
        // posistion에 해당하는 Place 객체를 가져옴
        val currentMonth = Calendar.getInstance().get(Calendar.MONTH)
        val place = placeList.value?.get(position)
        val salary = place?.salary?.get(currentMonth)
        val formattedSalary = NumberFormat.getNumberInstance().format(salary)
        // Place 객체의 각 속성을 ViewHolder의 뷰에 설정
        holder.binding.txtName.text = place?.name
        holder.binding.txtWageday.text = place?.wageday
        holder.binding.txtSalary.text = formattedSalary
        holder.binding.txtDaycount.text = place?.daycount.toString()

        // 수정버튼 클릭 이벤트, Place 객체의 속성을 번들에 담아 ChangeworkFragment로 네비게이션
        holder.binding.btnChageWork.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("name", place?.name)
            bundle.putString("workstartmonth", place?.workstartmonth)
            bundle.putString("workstartday", place?.workstartday)
            bundle.putString("wageday", place?.wageday)
            bundle.putString("hourlyrate", place?.hourlyrate)
            bundle.putInt("salary", place?.salary?.get(currentMonth) ?: 0)
            bundle.putInt("daycount", place?.daycount ?: 0)
            bundle.putIntArray("dayCalendarCheck", place?.dayCalendarCheck?.toIntArray() ?: IntArray(7))
            bundle.putString("starttime", place?.starttime)
            bundle.putString("endtime", place?.endtime)

            Navigation.findNavController(it).navigate(R.id.action_homeFragment_to_changeworkFragment, bundle)
        }
    }
    // 리사이클뷰에 표시할 항목의 개수를 반환하는 메서드
    override fun getItemCount() = placeList.value?.size ?: 0
}

//리사이클러뷰는 자체적으로 뷰 홀더의 재사용 메커니즘을 가지고 있어, 뷰홀더와 그에 연결된 바인딩 객체가 자동으로 메모리에서 해제됨