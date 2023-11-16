package com.example.op_projectapp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.op_projectapp.repository.PlaceRepository
import java.util.UUID

data class Place(
    val name: String = "",
    val salary: String = "0",
    val id: String = UUID.randomUUID().toString() // UUID를 사용하여 고유한 ID를 생성
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
