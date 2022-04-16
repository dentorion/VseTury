package com.entin.presentation.model

import java.io.Serializable

/**
 * TourDomainModel - model for domain level,
 * that describes business model of each tour
 */

data class TourDomainModel(
    val id: Int,
    val title: String,
    val citiesFrom: ArrayList<Int>,
    val citiesFromName: ArrayList<String>,
    val cityToName: String,
    val cityToId: Int,
    val dateFrom: String,
    val dateTo: String,
    val description: String,
    val price: Int,
    val company: Int,
    val contactPhone: String,
    val contactName: String,
) : Serializable
