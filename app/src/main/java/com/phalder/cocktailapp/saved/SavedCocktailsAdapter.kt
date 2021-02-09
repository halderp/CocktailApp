package com.phalder.cocktailapp.saved

import com.phalder.cocktailapp.R
import com.phalder.cocktailapp.repository.local.CockTailItem
import com.phalder.cocktailapp.utils.BaseRecyclerViewAdapter


//Use data binding to show the reminder on the item
class SavedCocktailsAdapter(callBack: (selectedCocktail: CockTailItem) -> Unit) :
    BaseRecyclerViewAdapter<CockTailItem>(callBack) {
    override fun getLayoutRes(viewType: Int) = R.layout.cocktail_list_item
}