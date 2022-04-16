package com.entin.data.model

import java.io.Serializable

/**
 * TourFirebaseModel - model for converting from firebase response
 * used in ":firebase" module to transform raw data
 */

data class ArticleFirebaseModel(
    val title: String,
    val text: String,
    val rate: Int,
    val images: ArrayList<String>,
    val id: Int,
    val date: Long,
    val comment: String = "",
    val actionButton: Map<String, String>
) : Serializable