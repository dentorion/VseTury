package com.entin.room.db.converter

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {

    /**
     * INTEGERS
     */

    @TypeConverter
    fun restoreListInteger(listOfString: String?): ArrayList<Int?>? =
        Gson().fromJson(listOfString, object : TypeToken<List<String?>?>() {}.type)

    @TypeConverter
    fun saveListInteger(listOfString: ArrayList<Int?>?): String? =
        Gson().toJson(listOfString)

    /**
     * STRINGS
     */

    @TypeConverter
    fun restoreListString(listOfString: String?): ArrayList<String?>? =
        Gson().fromJson(listOfString, object : TypeToken<List<String?>?>() {}.type)

    @TypeConverter
    fun saveListString(listOfString: ArrayList<String?>?): String? =
        Gson().toJson(listOfString)
}