package com.entin.room.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.entin.room.db.dao.ToursDAO
import com.entin.room.db.entity.TourRoomModel

//TODO: change entity class name entities = [TourRoomModel::class]

@Database(entities = [TourRoomModel::class], version = 1, exportSchema = false)
abstract class Db : RoomDatabase() {

    abstract fun newsDao(): ToursDAO

    companion object {
        const val DATABASE_NAME: String = "VseTuryDatabase"
    }
}