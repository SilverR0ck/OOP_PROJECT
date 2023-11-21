package com.example.op_projectapp.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.op_projectapp.Place
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class PlaceRepository {
    // 파이어베이스에서 데이터베이스 인스턴스
    val database = Firebase.database

    // places 노드에 대한 참조
    val WorkRef = database.getReference("places")

    // places 노드의 데이터 변화를 옵저브하는 메소드
    fun observePlaces(places: MutableLiveData<List<Place>>) {
        WorkRef.addValueEventListener(object : ValueEventListener {
            // 데이터가 변화할 때마다 호출되는 메소드
            override fun onDataChange(snapshot: DataSnapshot) {
                val placeList = mutableListOf<Place>()
                for (placeSnapshot in snapshot.children) { //// places 노드의 모든 자식 노드를 순회
                    // 자식 노드의 데이터를 Place 객체로 변환하고, 결과가 널이 아니면 리스트에 추가
                    placeSnapshot.getValue(Place::class.java)?.let { placeList.add(it) }
                } //만약 결과가 널이라면 let 블록을 건너뛰고 다음 반복으로 넘어감
                places.postValue(placeList) //변환된 Place 객체의 리스트를 라이브데이터에 전달
            }

            // 데이터 변화를 가져오는데 실패했을 때 호출되는 메소드
            override fun onCancelled(error: DatabaseError) {
                // 오류 메시지를 로그에 출력합니다.
                Log.e("PlaceRepository", "Failed to load places", error.toException())
            }
        })
    }

    fun postPlaces(placeList: List<Place>) {
        WorkRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val existingIds = mutableListOf<String>()
                for (placeSnapshot in snapshot.children) {
                    val place = placeSnapshot.getValue(Place::class.java)
                    place?.name?.let { existingIds.add(it) }
                }

                for (place in placeList) {
                    if (place.name !in existingIds) {
                        WorkRef.child(place.name).setValue(place)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("PlaceRepository", "Failed to load places", error.toException()) //예외처리
            }
        })
    }

    fun updatePlace(place: Place) {
        if (place.name.isNotEmpty()) {
            WorkRef.child(place.name).setValue(place)
        }
    }

    fun deletePlace(key: String) {
        Log.d("PlaceRepository", "Deleting place with key: $key")
        WorkRef.child(key).removeValue()
    }

}