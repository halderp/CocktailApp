package com.phalder.cocktailapp.repository.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters


@Database(entities = [CockTailItem::class], version = 1)
@TypeConverters(DBConverters::class)
abstract class CocktailDatabase: RoomDatabase() {

    // Data Access Object
    abstract fun cocktailDBDao(): CocktailDBDao

    /**
     * Define a companion object, this allows us to add functions on the AsteroidDatabase class.
     *
     * For example, clients can call `CocktailDatabase.getInstance(context)` to instantiate a new CocktailDatabase.
     */
    companion object{
        @Volatile
        private lateinit var INSTANCE: CocktailDatabase

        /**
         * Helper function to get the database.
         * If a database has already been retrieved, the previous database will be returned.
         * Otherwise, create a new database.
         */
        fun getInstance(context: Context): CocktailDatabase{
            synchronized(this){

                if (!::INSTANCE.isInitialized)
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        CocktailDatabase::class.java,
                        "cocktail_database"
                    ).build()
            }

            return INSTANCE
        }
    }
}