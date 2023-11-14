package com.example.op_projectapp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.op_projectapp.repository.PlaceRepository

data class Place(
    val name: String = "",
    val salary: Int = 0
)

class PlaceViewModel : ViewModel() {
    private val _nameplace = MutableLiveData<List<Place>>()
    val nameplace: LiveData<List<Place>> get() = _nameplace

    private val repository = PlaceRepository() // PlaceRepository는 당신이 정의

    init {
        repository.observePlaces(_nameplace)
    }

    fun addPlace(place: Place) {
        val newList = _nameplace.value?.toMutableList() ?: mutableListOf()
        newList.add(place)
        repository.postPlaces(newList)
    }
}
