package com.phalder.cocktailapp.repository.local

import androidx.room.*

@Dao
interface CocktailDBDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavCocktailItem(favCockTailItem: CockTailItem)

    @Delete
    suspend fun deleteFavCocktailItem(favCockTailItem: CockTailItem)

    @Query("SELECT * FROM fav_cocktail_table")
    suspend fun getAllFavCocktails(): List<CockTailItem>

    @Update
    suspend fun updateCocktailItemInDB(cockTailItem: CockTailItem)

}