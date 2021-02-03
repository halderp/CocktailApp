package com.phalder.cocktailapp.repository.network

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.QueryMap


// Base URL for Random Cocktail
private const val  BASE_URL_RANDOM_COCKTAIL = "https://www.thecocktaildb.com/api/json/v1/1/"


/**
 * Build the Moshi object that Retrofit will be using, making sure to add the Kotlin adapter for
 * full Kotlin compatibility.
 */
private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

/**
 * Use the Retrofit builder to build a retrofit object using a Moshi converter with our Moshi for APOD Endpoint
 * Enable logging to see request/response body
 */
private val clientRandomCocktail = OkHttpClient().newBuilder().addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)).build()

private val retrofitRandomCocktail = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .client(clientRandomCocktail)
    .baseUrl(BASE_URL_RANDOM_COCKTAIL)
    .build()

interface RandomCocktailApiService {
    //GET https://www.thecocktaildb.com/api/json/v1/1/random.php
    @GET("random.php")
    suspend fun getRandomCocktail(): Response<CockTail>
}

object CocktailApi {
    val randomCockTailService : RandomCocktailApiService by lazy { retrofitRandomCocktail.create(RandomCocktailApiService::class.java) }
}