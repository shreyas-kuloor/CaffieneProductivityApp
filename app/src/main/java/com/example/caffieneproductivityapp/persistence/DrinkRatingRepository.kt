package com.example.caffieneproductivityapp.persistence

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData

class DrinkRatingRepository(private val ratingDao: RatingDao, private val drinkDao: DrinkDao) {
    val allRatings: LiveData<List<Rating>> = ratingDao.getAllRatings()
    val allDrinks: LiveData<List<Drink>> = drinkDao.getAllDrinks()

    @WorkerThread
    fun insertRating(rating: Rating) {
        ratingDao.insert(rating)
    }

    @WorkerThread
    fun insertDrink(drink: Drink) {
        drinkDao.insert(drink)
    }

    fun getLastDrink() : LiveData<Drink> {
        return drinkDao.getLastDrink()
    }

    fun getRatingsForDrink(drinkId: Int) : LiveData<List<Rating>> {
        return ratingDao.findRatingsForDrink(drinkId)
    }

}