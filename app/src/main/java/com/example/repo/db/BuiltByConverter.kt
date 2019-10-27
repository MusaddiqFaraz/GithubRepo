package com.example.repo.db

import androidx.room.TypeConverter
import com.example.repo.githubapi.BuiltBy
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


class BuiltByConverter {

    @TypeConverter
    fun fromBuiltByList(builtBy: List<BuiltBy?>?): String? {
        if (builtBy == null) {
            return null
        }
        val gson = Gson()
        val type = object : TypeToken<List<BuiltBy>>() {

        }.getType()
        return gson.toJson(builtBy, type)
    }

    @TypeConverter
    fun toBuiltByList(builtByString: String?): List<BuiltBy?>? {
        if (builtByString == null) {
            return null
        }
        val gson = Gson()
        val type = object : TypeToken<List<BuiltBy>>() {

        }.getType()
        return gson.fromJson(builtByString, type)
    }
}