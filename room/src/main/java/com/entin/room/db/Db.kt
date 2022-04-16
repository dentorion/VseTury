package com.entin.room.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.entin.room.db.converter.Converters
import com.entin.room.db.dao.ToursDAO
import com.entin.room.db.entity.FavouriteTourRoomModel


@Database(entities = [FavouriteTourRoomModel::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class Db : RoomDatabase() {

    abstract fun newsDao(): ToursDAO

    companion object {
        const val DATABASE_NAME: String = "VseTuryDatabase"
    }
}