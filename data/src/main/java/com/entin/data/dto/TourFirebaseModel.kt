package com.entin.data.dto

data class TourFirebaseModel(
    val id: Int,
    val title: String,
    val cityStart: String,
    val cityFinish: String,
    val dateStart: Long,
    val dateFinish: Long,
    // TODO: HashSet or Set
    val citiesMiddle: String,
    val description: String,
    val price: Int,
    val company: Int,
)
