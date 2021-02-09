package com.phalder.cocktailapp.home

import android.app.Application
import androidx.lifecycle.*
import com.phalder.cocktailapp.repository.CockTailRepository
import com.phalder.cocktailapp.repository.local.CockTailItem
import com.phalder.cocktailapp.repository.local.CocktailDatabase
import com.phalder.cocktailapp.repository.network.Drink
import kotlinx.coroutines.launch

class HomeViewModel(application: Application): AndroidViewModel(application) {

    // Database and Repository
    private val cocktailDatabase = CocktailDatabase.getInstance(application)
    private val cockTailRepository = CockTailRepository(cocktailDatabase)

    // The internal MutableLiveData data & the public LiveData to capture Random Cocktail
    private val _randomCockTail = MutableLiveData<Drink>()

    // The internal MutableLiveData data & the public LiveData to capture Random Cocktail
    val randomCockTailItem : LiveData<CockTailItem>  = Transformations.map(_randomCockTail){
        var ingredients = createIngredients(it)
        var measures = createmeasures(it)
        CockTailItem(
            it.idDrink,
            it.strDrink,
            it.strInstructions,
            it.strCategory,
            it.strDrinkThumb,
            it.strAlcoholic,
            it.strGlass,
            ingredients,
            measures
        )
    }

    private fun createmeasures(drink: Drink): List<String> {
        var list = mutableListOf<String>()
        if (drink.strMeasure1 != null)
            list.add(drink.strMeasure1)
        if (drink.strMeasure2 != null)
            list.add(drink.strMeasure2)
        if (drink.strMeasure3 != null)
            list.add(drink.strMeasure3)
        if (drink.strMeasure4 != null)
            list.add(drink.strMeasure4)

        return list
    }

    private fun createIngredients(drink: Drink): List<String> {

        var list = mutableListOf<String>()
        if (drink.strIngredient1 != null)
            list.add(drink.strIngredient1)
        if (drink.strIngredient2 != null)
            list.add(drink.strIngredient2)
        if (drink.strIngredient3 != null)
            list.add(drink.strIngredient3)
        if (drink.strIngredient4 != null)
            list.add(drink.strIngredient4)

        return list
    }


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