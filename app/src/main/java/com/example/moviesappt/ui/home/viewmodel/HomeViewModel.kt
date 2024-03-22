package com.example.moviesappt.ui.home.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moviesappt.data.dao.MovieDao
import com.example.moviesappt.domain.model.ResultsItem
import com.example.moviesappt.domain.utils.ResponseState
import com.example.moviesapptask.domain.usecase.MoviesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject
import java.util.concurrent.TimeUnit
import androidx.work.*
import android.content.Context
import com.example.moviesappt.worker.CacheInvalidationWorker

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val useCase: MoviesUseCase,
    private val movieDao: MovieDao,
    private val context: Context
) : ViewModel() {

    private val _state = MutableStateFlow<MovieState>(MovieState())
    val movies: StateFlow<MovieState>
        get() = _state

    init {
        fetchMovies()
        schedulePeriodicCacheInvalidation()
    }

    private fun fetchMovies() {
        viewModelScope.launch {
            movieDao.getMovies()
                .onCompletion { cause ->
                    cause?.let {
                        Log.e("HomeViewModel", "Error fetching movies from DB: $it")
                    }
                }
                .collect { movies ->
                    if (movies.isNotEmpty()) {
                        _state.value = MovieState(movie = movies)
                    } else {
                        getMovies("28", 1)
                    }
                }
        }
    }

    fun getMovies(genersId: String, page: Int) {
        viewModelScope.launch {
            useCase.invoke(genersId = genersId, page = page)
                .onEach { result ->
                    when (result) {
                        is ResponseState.Success -> {
                            val movies = result.data.map { movie ->
                                ResultsItem(movie.id.toString(), movie.title)
                            }
                            movieDao.insertAll(movies)
                            _state.value = MovieState(movie = result.data)
                        }
                        is ResponseState.Failed -> {
                            Log.e("HomeViewModel", "Error fetching movies: ${result.e.message}")
                            _state.value = MovieState(error = result.e.message ?: "An unexpected error occurred")
                        }
                        ResponseState.Loading -> {
                            _state.value = MovieState(isLoading = true)
                        }
                    }
                }.launchIn(viewModelScope)
        }
    }

    private fun schedulePeriodicCacheInvalidation() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val repeatingRequest = PeriodicWorkRequestBuilder<CacheInvalidationWorker>(
            4, TimeUnit.HOURS
        ).setConstraints(constraints)
            .build()

        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            "cache_invalidation_worker",
            ExistingPeriodicWorkPolicy.REPLACE,
            repeatingRequest
        )
    }
}


data class MovieState(
    val isLoading: Boolean = false,
    val movie: List<ResultsItem>? = null,
    val error: String = "",
)