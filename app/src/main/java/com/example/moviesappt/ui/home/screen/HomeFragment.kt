package com.example.moviesappt.ui.home.screen

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.moviesappt.databinding.FragmentHomeBinding
import com.example.moviesappt.ui.home.adapter.MoviesAdapter
import com.example.moviesappt.ui.home.viewmodel.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val viewModel: HomeViewModel by viewModels()
    private var adapter: MoviesAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = MoviesAdapter(null, requireContext())
        binding.moviesRecycler.adapter = adapter

        viewModel.movies.onEach { movieState ->
            when {
                movieState.isLoading -> {
                    // Handle loading state
                    // You may want to show a loading indicator
                    Log.d("HomeFragment", "Loading movies...")
                }

                movieState.error.isNotEmpty() -> {
                    // Handle error state
                    // Show an error message
                    Log.e("HomeFragment", "Error: ${movieState.error}")
                }

                else -> {
                    // Handle success state
                    // Update the adapter with the new movie list
                    movieState.movie?.let { movies ->
                        if (movies.isNotEmpty()) {
                            // Movies are available in the database
                            adapter?.changeList(movies)
                            Log.e("HomeFragment", "Movies loaded from DB: ${movies.size}")
                        } else {
                            // Movies not available in the database, fetch from API
                            viewModel.getMovies("28", 1)
                        }
                    }
                }
            }
        }.launchIn(viewLifecycleOwner.lifecycleScope)
    }
}