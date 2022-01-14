package com.entin.room.db.dao

import androidx.room.Dao
import androidx.room.Query
import com.entin.room.db.entity.TourRoomModel
import kotlinx.coroutines.flow.Flow

/**
 * DAO class for Tour model
 * TODO: write real queries to Room DB
 */

@Dao
interface ToursDAO {

    // GET news
    @Query("SELECT * FROM tours")
    fun getTours(): Flow<List<TourRoomModel>>

}