package com.example.op_projectapp.Model

data class Place(
    // 필드의 초기값이 실제 의미를 가지는 값이 아니면, 단순히 null을 피하기 위한 임시값인지 고려
    val name: String? = null,
    val workstartmonth:String ? = null,
    val workstartday: String ? = null,
    val wageday: String? = null,
    val starttime: String? = null,
    val endtime: String? = null,
    val salary: List<Int>? = MutableList(12) { 0 },
    val daycount: Int? = null,
    val hourlyrate: String? = null,
    val dayCalendarCheck: List<Int> ?= mutableListOf(),
)