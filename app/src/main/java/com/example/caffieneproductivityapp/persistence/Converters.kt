package com.example.caffieneproductivityapp.persistence

import androidx.room.TypeConverter
import com.example.caffieneproductivityapp.DrinkSize

class Converters {
    @TypeConverter
    fun fromDrinkSize(size: DrinkSize?) : String? {
        return size?.toString()
    }

    @TypeConverter
    fun stringToDrinkSize(sizeString: String) : DrinkSize? {
        return DrinkSize.valueOf(sizeString)
    }
}