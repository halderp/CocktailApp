package com.phalder.cocktailapp.repository

import com.phalder.cocktailapp.repository.local.CockTailItem
import com.phalder.cocktailapp.repository.local.CocktailDatabase
import com.phalder.cocktailapp.repository.network.CocktailApi
import com.phalder.cocktailapp.repository.network.Drink
import timber.log.Timber

// Repository takes Database as dependency
class CockTailRepository(var cocktailDatabase: CocktailDatabase) {

    // This function gets the random cocktail
    suspend fun getRandomCocktail(): Drink? {
        Timber.d("CockTailRepository: Start getRandomCocktail() --> Fetch Random Cocktail")

        try {
            val response = CocktailApi.randomCockTailService.getRandomCocktail()
            val cockTails = response.body()
            Timber.d("CockTailRepository: getRandomCocktail() --> Recieved Data")
            return cockTails?.drinks?.get(0)
        } catch (e: Exception) {
            Timber.e("CockTailRepository: Exception getRandomCocktail() --> " +  e.message)
        }
        return null
    }

    suspend fun insertFavCocktailItem(favCocktail : CockTailItem){
        cocktailDatabase.cocktailDBDao().insertFavCocktailItem(favCocktail)
    }

    suspend fun deleteFavCocktailItem(favCocktail : CockTailItem){
        cocktailDatabase.cocktailDBDao().deleteFavCocktailItem(favCocktail)
    }

    suspend fun getAllFavCocktailFromDB(): List<CockTailItem>{
        return cocktailDatabase.cocktailDBDao().getAllFavCocktails()
    }
}