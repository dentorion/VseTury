package com.entin.room.db.dao

import androidx.room.*
import com.entin.room.db.entity.FavouriteTourRoomModel
import kotlinx.coroutines.flow.Flow

/**
 * DAO class for Room
 */

@Dao
interface ToursDAO {
    // GET news
    @Query("SELECT * FROM favourite_tours")
    fun getAllFavouriteTours(): Flow<List<FavouriteTourRoomModel>>

    // Add favourite tour
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addFavouriteTour(tour: FavouriteTourRoomModel)

    // Delete favourite tour
    @Query("DELETE FROM favourite_tours WHERE favouriteTourId = :tourId")
    suspend fun delFavouriteTour(tourId: Int)

    // Get favourite state of tour
    @Query("SELECT * FROM favourite_tours WHERE favouriteTourId = :tourId LIMIT 1")
    fun getFavouriteStatusOfTour(tourId: Int): Flow<FavouriteTourRoomModel?>
}