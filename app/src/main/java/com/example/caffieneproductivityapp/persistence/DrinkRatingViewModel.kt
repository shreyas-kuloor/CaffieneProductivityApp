package com.example.caffieneproductivityapp.persistence

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DrinkRatingViewModel(app: Application) : AndroidViewModel(app) {
    private val repository : DrinkRatingRepository
    val allRatings: LiveData<List<Rating>>
    val allDrinks: LiveData<List<Drink>>

    init {
        val ratingsDao = DrinkRatingDatabase.getDatabase(app).ratingDao()
        val drinksDao = DrinkRatingDatabase.getDatabase(app).drinkDao()
        repository = DrinkRatingRepository(ratingsDao, drinksDao)
        allRatings = repository.allRatings
        allDrinks = repository.allDrinks
    }

    fun insertRating(rating: Rating) = viewModelScope.launch(Dispatchers.IO) {
        repository.insertRating(rating)
    }

    fun insertDrink(drink: Drink) = viewModelScope.launch(Dispatchers.IO) {
        repository.insertDrink(drink)
    }

    fun getLastDrink() : LiveData<Drink> {
        return repository.getLastDrink()
    }

    fun getRatingsForDrink(drinkId: Int) : LiveData<List<Rating>> {
        return repository.getRatingsForDrink(drinkId)
    }
}