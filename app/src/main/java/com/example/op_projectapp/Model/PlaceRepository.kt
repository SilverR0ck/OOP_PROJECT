package com.example.op_projectapp.Model

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
// 데이터 소스와 데이터를 관리하는 클래스, 데이터의 로컬 저장소와 원격 데이터 소스간 중간 매개체 역할
class PlaceRepository {
    val database get() = Firebase.database
    val PlaceRef get() = database.getReference("places")
    // places 노드의 데이터 변화를 옵저브하는 메소드
    fun observePlaces(places: MutableLiveData<List<Place>>) {
        PlaceRef.addValueEventListener(object : ValueEventListener {
            // 데이터가 변화할 때마다 호출되는 메소드
            override fun onDataChange(snapshot: DataSnapshot) {
                val placeList = mutableListOf<Place>()
                for (placeSnapshot in snapshot.children) {
                    // 자식 노드의 데이터를 Place 객체로 변환하고, 결과가 널이 아니면 리스트에 추가
                    placeSnapshot.getValue(Place::class.java)?.let { placeList.add(it) }
                }
                places.postValue(placeList)
            }
            // 데이터 변화를 가져오는데 실패했을 때 호출되는 메소드
            override fun onCancelled(error: DatabaseError) {
                Log.e("PlaceRepository", "Failed to load places", error.toException())
            }
        })
    }

    // place 노드에 리스트의 Place 객체를 추가
    fun setAddPlace(place: Place) {
        PlaceRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val existingIds = mutableListOf<String>()
                for (placeSnapshot in snapshot.children) {
                    val existingPlace = placeSnapshot.getValue(Place::class.java)
                    existingPlace?.name?.let { existingIds.add(it) }
                }
                // 중복 추가 방지
                if (place.name !in existingIds) {
                    PlaceRef.child(place.name ?: " ").setValue(place)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("PlaceRepository", "Failed to load places", error.toException())
            }
        })
    }

    // place 객체를 업데이트 하는 함수
    fun setUpdatePlace(place: Place) {
        if (place.name!!.isNotEmpty()) {
            PlaceRef.child(place.name?:" ").setValue(place)
        }
    }

    // 특정 Place 객체를 삭제하는 함수, key값으로 노드의 이름을 받아와서 삭제 진행
    fun setDeletePlace(name: String) {
        Log.d("PlaceRepository", "Deleting place with key: $name")
        PlaceRef.child(name).removeValue()
    }



}