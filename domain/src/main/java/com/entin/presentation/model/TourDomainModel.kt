package com.entin.presentation.model

data class TourDomainModel(
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