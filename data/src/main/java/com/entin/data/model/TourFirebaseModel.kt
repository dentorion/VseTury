package com.entin.data.model

import java.io.Serializable

/**
 * TourFirebaseModel - model for converting from firebase response
 * used in ":firebase" module to transform raw data
 */

data class TourFirebaseModel(
    val id: Int,
    val title: String,
    val citiesFrom: ArrayList<Int>,
    val cityTo: Int,
    val dateFrom: Long,
    val dateTo: Long,
    val description: String,
    val price: Int,
    val company: Int,
    val contactPhone: String,
    val contactName: String,
): Serializable