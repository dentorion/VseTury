package com.entin.room.db.entity

import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.entin.room.db.entity.TourRoomModel.Companion.TABLE_NAME

/**
 * Entity for Room of TourDomainModel
 * TODO: Put real fields
 */

@Entity(tableName = TABLE_NAME)
data class TourRoomModel(

    @PrimaryKey(autoGenerate = true)
    @NonNull
    val id: Int,
) {

    companion object {
        const val TABLE_NAME = "tours"
    }
}