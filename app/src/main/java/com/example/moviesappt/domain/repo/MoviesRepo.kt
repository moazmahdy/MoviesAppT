package com.example.moviesappt.domain.repo

import com.example.moviesappt.domain.model.ResultsItem
import com.example.moviesappt.domain.utils.ResponseState
import kotlinx.coroutines.flow.Flow


interface MoviesRepo {

    suspend fun getMovies(
        genersId: String,
        page: Int
    ): Flow<ResponseState<List<ResultsItem>>>
}