package com.example.op_projectapp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.op_projectapp.repository.PlaceRepository
import java.util.UUID

data class Place(
    val id: String = UUID.randomUUID().toString(), // UUID를 사용하여 고유한 ID를 생성
    val name: String = "스타벅스",
    val wageday: String = "1",
    val wageamount: String = "0",
    val starttime: String = "0",
    val endtime: String = "24",
    val salary: Int? = 0,
    val daycount: Int = 0,
    val dayCalendarCheck: List<Int> = mutableListOf(),
   // val workDays: List<Boolean> = List(7) { false },  // 월요일부터 일요일까지 각 요일에 대해 일하는지 여부

)

class PlaceViewModel : ViewModel() {
    private val _nameplace = MutableLiveData<List<Place>>()
    val nameplace: LiveData<List<Place>> get() = _nameplace

    private val repository = PlaceRepository() //

    init {
        repository.observePlaces(_nameplace)
    }

    fun addPlace(place: Place) {
        val newList = _nameplace.value?.toMutableList() ?: mutableListOf()
        newList.add(place)
        repository.postPlaces(newList)
    }

}