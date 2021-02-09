package com.phalder.cocktailapp.details

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.phalder.cocktailapp.repository.local.CockTailItem

class CockTailDetailVMFactory(private val application: Application, private val cockTailItem: CockTailItem,private val isAvailableInDb:Boolean) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CocktailDetailViewModel::class.java)) {
            return CocktailDetailViewModel(application,cockTailItem,isAvailableInDb) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}