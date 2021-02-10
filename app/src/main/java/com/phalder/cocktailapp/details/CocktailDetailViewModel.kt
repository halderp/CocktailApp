package com.phalder.cocktailapp.details

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.phalder.cocktailapp.R
import com.phalder.cocktailapp.repository.CockTailRepository
import com.phalder.cocktailapp.repository.local.CockTailItem
import com.phalder.cocktailapp.repository.local.CocktailDatabase
import com.phalder.cocktailapp.utils.SingleLiveEvent
import kotlinx.coroutines.launch

class CocktailDetailViewModel(application: Application,cockTailItem: CockTailItem,isAvailableInDb:Boolean): AndroidViewModel(application) {

    var cockTailItemLiveData: MutableLiveData<CockTailItem> = MutableLiveData(cockTailItem)
    // The internal MutableLiveData data & the public LiveData to capture Picture of Day
    private val _cockTailImgUrl = MutableLiveData<String>()
    val cockTailImgUrl : LiveData<String>
        get() =_cockTailImgUrl

    // Database and Repository
    private val cocktailDatabase = CocktailDatabase.getInstance(application)
    private val cockTailRepository = CockTailRepository(cocktailDatabase)

    // utilies
    val showSnackBarInt: SingleLiveEvent<Int> = SingleLiveEvent()

    // The internal MutableLiveData to control the fav icon
    private val _isAvailableInFavDB = MutableLiveData<Boolean>()
    val isAvailableInFavDB : LiveData<Boolean>
        get() =_isAvailableInFavDB

    init {
        _isAvailableInFavDB.value = isAvailableInDb
        _cockTailImgUrl.value = cockTailItemLiveData.value!!.thumbnail
    }

    fun createIngredientsMeasure(): String {
        var returnValue : String =""
        var count:Int = 0
        for(ingredient in cockTailItemLiveData.value!!.ingredients){
            val item = ingredient + " : " + cockTailItemLiveData.value!!.measures[count++]
            returnValue += item + "\n"

        }
        return returnValue
    }

    fun itemSaveUnsaveInFabDB() {
        if (_isAvailableInFavDB.value == false) {
            viewModelScope.launch {
                cockTailRepository.insertFavCocktailItem(cockTailItemLiveData.value!!)
            }
            _isAvailableInFavDB.value = true
            showSnackBarInt.value = R.string.item_saved_string
        } else {
            viewModelScope.launch {
                cockTailRepository.deleteFavCocktailItem(cockTailItemLiveData.value!!)
            }
            _isAvailableInFavDB.value = false
            showSnackBarInt.value = R.string.item_deleted_string
        }
    }

    fun updateCocktailImage(newImage: String){
        viewModelScope.launch {
            cockTailItemLiveData.value!!.thumbnail = newImage
            cockTailRepository.updateCocktailItem(cockTailItemLiveData.value!!)
            _cockTailImgUrl.value = cockTailItemLiveData.value!!.thumbnail
        }
    }
}