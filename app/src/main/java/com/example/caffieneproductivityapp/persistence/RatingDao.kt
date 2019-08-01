package com.example.caffieneproductivityapp.persistence

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface RatingDao {
    @Insert
    fun insert(rating: Rating)

    @Update
    fun update(vararg ratings: Rating)

    @Delete
    fun delete(vararg ratings: Rating)

    @Query("DELETE FROM rating")
    fun deleteAll()

    @Query("SELECT * FROM rating")
    fun getAllRatings() : LiveData<List<Rating>>

    @Query("SELECT * FROM rating WHERE drink_id=:drinkId")
    fun findRatingsForDrink(drinkId : Int) : LiveData<List<Rating>>
}