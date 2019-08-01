package com.example.caffieneproductivityapp.persistence

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface DrinkDao {
    @Query("SELECT * from drink ORDER BY id DESC")
    fun getAllDrinks(): LiveData<List<Drink>>

    @Insert
    fun insert(drink: Drink) : Long

    @Update
    fun update(vararg drink: Drink)

    @Delete
    fun delete(vararg drink: Drink)

    @Query("DELETE FROM drink")
    fun deleteAll()

    @Query("SELECT * FROM drink ORDER BY id DESC LIMIT 1")
    fun getLastDrink() : LiveData<Drink>
}