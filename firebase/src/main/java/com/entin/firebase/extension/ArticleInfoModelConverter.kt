@file:Suppress("UNCHECKED_CAST")

package com.entin.firebase.extension

import com.entin.data.model.ArticleFirebaseModel
import com.entin.data.model.TourFirebaseModel
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.google.firebase.firestore.ktx.getField

/**
 * Converter to transform raw firebase data into [TourFirebaseModel] models
 */

fun QueryDocumentSnapshot.convertToArticleFirebaseModel() = ArticleFirebaseModel(
    title = this.getString(TITLE) ?: "",
    text = this.getString(TEXT) ?: "",
    rate = this.getField<Int>(RATE) ?: 0,
    images = this.get(IMAGES) as ArrayList<String>,
    id = this.getField<Int>(ID) ?: 0,
    date = this.getTimestamp(DATE)?.toDate()?.time ?: 0,
    actionButton = this.get(ACTION_BUTTON) as Map<String, String>,
)

private const val TITLE = "title"
private const val TEXT = "text"
private const val RATE = "rate"
private const val IMAGES = "images"
private const val ID = "id"
private const val DATE = "date"
private const val ACTION_BUTTON = "actionButton"