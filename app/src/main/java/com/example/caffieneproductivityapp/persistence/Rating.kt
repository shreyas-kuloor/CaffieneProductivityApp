package com.example.caffieneproductivityapp.persistence

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.PrimaryKey

@Entity(foreignKeys = [ForeignKey(entity = Drink::class, parentColumns = ["id"], childColumns = ["drink_id"], onDelete = CASCADE)])
data class Rating (
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo(name = "time_stamp") val timeStamp : Long,
    @ColumnInfo(name = "productivity") val productivityRating : Int,
    @ColumnInfo(name = "drink_id") val drinkId : Int?
)