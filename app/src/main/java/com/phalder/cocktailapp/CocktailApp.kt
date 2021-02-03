package com.phalder.cocktailapp

import android.app.Application
import timber.log.Timber

class CocktailApp: Application() {


    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
    }
}