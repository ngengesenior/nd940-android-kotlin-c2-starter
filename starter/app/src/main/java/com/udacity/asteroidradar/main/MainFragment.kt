package com.udacity.asteroidradar.main

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController

import com.squareup.picasso.Picasso
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.adapters.AsteroidViewHolder
import com.udacity.asteroidradar.adapters.AsteroidsAdapter
import com.udacity.asteroidradar.adapters.OnAsteroidClickListener
import com.udacity.asteroidradar.databinding.FragmentMainBinding
import timber.log.Timber

class MainFragment : Fragment() {

    private val viewModel: MainViewModel by lazy {
        ViewModelProvider(this).get(MainViewModel::class.java)
    }
    private lateinit var adapter:AsteroidsAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val binding = FragmentMainBinding.inflate(inflater)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        adapter = AsteroidsAdapter(emptyList(), OnAsteroidClickListener {
            val action = MainFragmentDirections.actionShowDetails(it)
            findNavController().navigate(action)
        })

        binding.asteroidRecycler.adapter = adapter
        viewModel.allAsteroids.observe(viewLifecycleOwner,Observer {
            Timber.d("Adapter data $it")
            adapter.setList(it)
            binding.statusLoadingWheel.visibility = View.GONE
        })

        viewModel.imageOfTheDay.observe(viewLifecycleOwner, Observer {
            Picasso.get().load(it.url)
                .into(binding.activityMainImageOfTheDay)
        })
        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_overflow_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return true
    }
}
