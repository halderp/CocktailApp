package com.phalder.cocktailapp.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.android.material.snackbar.Snackbar
import com.phalder.cocktailapp.R
import com.phalder.cocktailapp.databinding.FragmentCocktailDetailBinding
import timber.log.Timber


class CocktailDetailFragment : Fragment() {

    // Binding Objects
    private lateinit var binding: FragmentCocktailDetailBinding
    //View Model
    private lateinit var viewModel: CocktailDetailViewModel

    override fun onCreateView(inflater: LayoutInflater,  container: ViewGroup?,savedInstanceState: Bundle?
    ): View? {

        binding = FragmentCocktailDetailBinding.inflate(inflater)
        binding.lifecycleOwner = this

        // ViewModel
        val argument = CocktailDetailFragmentArgs.fromBundle(requireArguments())

        val viewModelFactory = CockTailDetailVMFactory(requireActivity().application,argument.cocktailItem, argument.isAvailableInDb)
        viewModel = ViewModelProvider(this,viewModelFactory).get(CocktailDetailViewModel::class.java)
        binding.detailViewModel = viewModel

        // save or unsave item from Fav DB
        binding.fab.setOnClickListener { view ->
            viewModel.itemSaveUnsaveInFabDB()
        }

        viewModel.showSnackBarInt.observe(this, Observer {
            Snackbar.make(this.requireView(), getString(it), Snackbar.LENGTH_LONG).show()
        })

        return binding.root
    }

    // Set up toolbar for this fragment
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val layout = view.findViewById<CollapsingToolbarLayout>(R.id.collapsing_toolbar_layout)
        val toolbar = view.findViewById<Toolbar>(R.id.toolbar)

        val navController = findNavController()
        val appBarConfiguration = AppBarConfiguration(navController.graph)

        layout.setupWithNavController(toolbar, navController, appBarConfiguration)
    }
}