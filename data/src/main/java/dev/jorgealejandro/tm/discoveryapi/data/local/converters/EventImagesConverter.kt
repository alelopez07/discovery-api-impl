package dev.jorgealejandro.tm.discoveryapi.data.local.converters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dev.jorgealejandro.tm.discoveryapi.core.dto.entities.EventImageEntity

class EventImagesConverter {
    private val gson = Gson()
    @TypeConverter
    fun fromImages(images: List<EventImageEntity>): String {
        return gson.toJson(images)
    }

    @TypeConverter
    fun toImages(data: String): List<EventImageEntity> {
        val listType = object : TypeToken<List<EventImageEntity>>() {}.type
        return gson.fromJson(data, listType)
    }
}