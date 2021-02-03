package com.phalder.cocktailapp.repository

import com.phalder.cocktailapp.repository.network.CocktailApi
import com.phalder.cocktailapp.repository.network.Drink
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import timber.log.Timber

class CockTailRepository {

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
}