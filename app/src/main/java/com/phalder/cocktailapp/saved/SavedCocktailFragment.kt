package com.phalder.cocktailapp.saved

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.phalder.cocktailapp.databinding.SavedCocktailFragmentBinding
import com.phalder.cocktailapp.utils.setup
import timber.log.Timber

class SavedCocktailFragment : Fragment() {

    private lateinit var viewModel: SavedCocktailViewModel
    private lateinit var binding: SavedCocktailFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = SavedCocktailFragmentBinding.inflate(inflater)

        // Set up VieweModel
        viewModel = ViewModelProvider(this).get(SavedCocktailViewModel::class.java)
        binding.viewModel = viewModel

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        viewModel.loadSavedCocktails()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.lifecycleOwner = this
        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        val adapter = SavedCocktailsAdapter {
            // do nothing now
            Timber.d(it.name)
            findNavController().navigate(
                SavedCocktailFragmentDirections.actionSavedCocktailFragmentToCocktailDetailFragment(it,true)
            )
        }
        binding.favCocktailsRecyclerView.setup(adapter)
    }

}