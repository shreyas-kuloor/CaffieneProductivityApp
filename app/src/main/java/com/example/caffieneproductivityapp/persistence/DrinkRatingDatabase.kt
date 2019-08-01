package com.example.caffieneproductivityapp.persistence

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [Rating::class, Drink::class], version = 2)
@TypeConverters(Converters::class)
abstract class DrinkRatingDatabase : RoomDatabase() {
    abstract fun ratingDao(): RatingDao
    abstract fun drinkDao(): DrinkDao

    companion object {
        @Volatile
        private var INSTANCE: DrinkRatingDatabase? = null

        fun getDatabase(context: Context): DrinkRatingDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(context.applicationContext, DrinkRatingDatabase::class.java, "Rating_database").fallbackToDestructiveMigration().build()
                INSTANCE = instance
                return instance
            }
        }
    }

}

