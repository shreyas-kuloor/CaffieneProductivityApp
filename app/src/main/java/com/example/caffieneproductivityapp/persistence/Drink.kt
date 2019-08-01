package com.example.caffieneproductivityapp.persistence
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.caffieneproductivityapp.DrinkSize

@Entity
data class Drink (
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo(name = "size") val size: DrinkSize,
    @ColumnInfo(name = "start_time") val startTime: Long
)