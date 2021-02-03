package com.phalder.cocktailapp.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.phalder.cocktailapp.repository.CockTailRepository
import com.phalder.cocktailapp.repository.network.Drink
import kotlinx.coroutines.launch

class HomeViewModel(application: Application): AndroidViewModel(application) {

    // Database and Repository
    private val cockTailRepository = CockTailRepository()

    // The internal MutableLiveData data & the public LiveData to capture Random Cocktail
    private val _randomCockTail = MutableLiveData<Drink>()
    val randomCockTail : LiveData<Drink>
        get() = _randomCockTail

    // initializer block
    init {
        // setRandomPicOfTheDay()
       getRandomCockTail()

    }

    private fun getRandomCockTail() {
        viewModelScope.launch {
            var drink =  cockTailRepository.getRandomCocktail()
            _randomCockTail.value = drink
        }
    }

}