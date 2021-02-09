package com.phalder.cocktailapp.saved

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.phalder.cocktailapp.repository.CockTailRepository
import com.phalder.cocktailapp.repository.local.CockTailItem
import com.phalder.cocktailapp.repository.local.CocktailDatabase
import kotlinx.coroutines.launch

class SavedCocktailViewModel(application: Application): AndroidViewModel(application) {

    // Database and Repository
    private val cocktailDatabase = CocktailDatabase.getInstance(application)
    private val cockTailRepository = CockTailRepository(cocktailDatabase)

    // list that holds the cocktail data items to be displayed on the UI
    val favCocktailList = MutableLiveData<List<CockTailItem>>()


    init {
        loadSavedCocktails()
    }
    /**
     * Get all the cocktails that are saved in Datavse and add them to the favCocktailList to be shown on the UI,
     * or show error if any
     */
    fun loadSavedCocktails() {

        viewModelScope.launch {
            //Get the list from DB
            val result = cockTailRepository.getAllFavCocktailFromDB()
            favCocktailList.value = result
       }
    }
}