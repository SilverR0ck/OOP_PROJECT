package com.example.op_projectapp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.op_projectapp.repository.PlaceRepository
import com.google.firebase.database.FirebaseDatabase
import java.util.UUID

data class Place(
    val name: String = " ",
    val wageday: String = "1",
    val starttime: String = "0",
    val endtime: String = "24",
    val salary: Int? = 0,
    val daycount: Int = 0,
    val hourlyrate: String = "1",
    val dayCalendarCheck: List<Int> = mutableListOf(),
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

