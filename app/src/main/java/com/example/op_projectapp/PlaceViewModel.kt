package com.example.op_projectapp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.op_projectapp.repository.PlaceRepository
import com.google.firebase.database.FirebaseDatabase
import java.util.UUID

data class Place(
    // 필드의 초기값이 실제 의미를 가지는 값이 아니면, 단순히 null을 피하기 위한 임시값인지 고려
    val name: String? = null,
    val wageday: String? = null,
    val starttime: String? = null,
    val endtime: String? = null,
    val salary: Int? = null,
    val daycount: Int? = null,
    val hourlyrate: String? = null,
    val dayCalendarCheck: List<Int> ?= mutableListOf(),
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

