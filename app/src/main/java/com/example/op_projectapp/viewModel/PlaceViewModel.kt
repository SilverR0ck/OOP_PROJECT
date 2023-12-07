package com.example.op_projectapp.viewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.op_projectapp.Model.Place
import com.example.op_projectapp.Model.PlaceRepository


class PlaceViewModel : ViewModel() {
    private val repository = PlaceRepository()
    private val _places = MutableLiveData<List<Place>>()
    val places: LiveData< List<Place> > get() = _places

    init {
        repository.observePlaces( _places )
    }

    fun setAddPlace(place: Place) = repository.setAddPlace(place)
    fun setUpdatePlace(place: Place) = repository.setUpdatePlace(place)
    fun setDeletePlace(name: String) = repository.setDeletePlace(name)
}

