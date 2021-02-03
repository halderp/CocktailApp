package com.phalder.cocktailapp.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.phalder.cocktailapp.R

class CocktailDetailFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_cocktail_detail, container, false)
    }

    // Set up toolbar for this fragment
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val layout =  view.findViewById<CollapsingToolbarLayout>(R.id.collapsing_toolbar_layout)
        val toolbar = view.findViewById<Toolbar>(R.id.toolbar)

        val navController = findNavController()
        val appBarConfiguration = AppBarConfiguration(navController.graph)

        layout.setupWithNavController(toolbar, navController, appBarConfiguration)
    }
}