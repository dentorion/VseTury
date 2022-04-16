@file:Suppress("UNCHECKED_CAST")

package com.entin.firebase.extension

import com.entin.data.model.TourFirebaseModel
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.google.firebase.firestore.ktx.getField

/**
 * Converter to transform raw firebase data into [TourFirebaseModel] models
 */

fun QueryDocumentSnapshot.convertToTourFirebaseModel() = TourFirebaseModel(
    id = this.getField<Int>(ID) ?: 0,
    title = this.getString(TITLE) ?: "",
    citiesFrom = this.get(CITIES_FROM) as ArrayList<Int>,
    cityTo = this.getField<Int>(CITY_TO) ?: 0,
    dateFrom = this.getTimestamp(DATE_FROM)?.toDate()?.time ?: 0,
    dateTo = this.getTimestamp(DATE_TO)?.toDate()?.time ?: 0,
    description = this.getString(DESCRIPTION) ?: "",
    price = this.getField<Int>(PRICE) ?: 0,
    company = this.getField<Int>(COMPANY) ?: 0,
    contactPhone = this.getString(CONTACT_PHONE) ?: "",
    contactName = this.getString(CONTACT_NAME) ?: "",
)

private const val ID = "id"
private const val TITLE = "title"
private const val CITIES_FROM = "citiesFrom"
private const val CITY_TO = "cityTo"
private const val DATE_FROM = "dateFrom"
private const val DATE_TO = "dateTo"
private const val DESCRIPTION = "description"
private const val PRICE = "price"
private const val COMPANY = "company"
private const val CONTACT_PHONE = "contactPhone"
private const val CONTACT_NAME = "contactName"
