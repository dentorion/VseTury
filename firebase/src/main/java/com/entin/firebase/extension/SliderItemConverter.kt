@file:Suppress("UNCHECKED_CAST")

package com.entin.firebase.extension

import com.entin.presentation.model.SliderItemModel
import com.google.firebase.firestore.QueryDocumentSnapshot

fun QueryDocumentSnapshot.convertToSliderItem() = SliderItemModel(
    http = this.getString(HTTP) ?: "",
    text = this.getString(TEXT) ?: "",
    action = this.get(ACTION) as Map<String, String>,
)

private const val HTTP = "http"
private const val TEXT = "text"
private const val ACTION = "action"