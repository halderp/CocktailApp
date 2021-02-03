package com.phalder.cocktailapp.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.phalder.cocktailapp.R
import com.phalder.cocktailapp.databinding.FragmentHomeBinding
import timber.log.Timber


class HomeFragment : Fragment() {

    // Binding Objects
    private lateinit var binding: FragmentHomeBinding

    //View Model
    private val viewModel: HomeViewModel by lazy {
        ViewModelProvider(this).get(HomeViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater)
        binding.lifecycleOwner = this
        binding.homeViewModel = viewModel

        // observe the Random Cocktal
        viewModel.randomCockTail.observe(viewLifecycleOwner, Observer {
            it?.let {
                Timber.d(it.strDrink)
            }
        })

        return binding.root
    }


    // Set up toolbar for this fragment
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val navController = findNavController()
        val appBarConfiguration = AppBarConfiguration(navController.graph)

        binding.toolbar.setupWithNavController(navController, appBarConfiguration)
    }

}