package com.entin.room.db.entity

import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.entin.room.db.entity.FavouriteTourRoomModel.Companion.TABLE_NAME
import java.io.Serializable
import java.util.*
import kotlin.collections.ArrayList

/**
 * Entity for Room.
 * For Like / dislike system.
 */

@Entity(tableName = TABLE_NAME)
data class FavouriteTourRoomModel(

    @PrimaryKey(autoGenerate = true)
    @NonNull
    val id: Int = 0,

    val favouriteTourId: Int,

    val favouriteTourTitle: String,

    val favouriteTourCitiesFrom: ArrayList<Int>,

    val favouriteTourCitiesFromName: ArrayList<String>,

    val favouriteTourCityToName: String,

    val favouriteTourCityToId: Int,

    val favouriteTourDateFrom: String,

    val favouriteTourDateTo: String,

    val favouriteStatus: Boolean,

    val date: Long = Date().time,
) {

    companion object {
        const val TABLE_NAME = "favourite_tours"
    }
}