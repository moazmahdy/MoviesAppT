package com.example.moviesappt.data.repoimpl

import com.example.moviesappt.data.remote.MoviesApi
import com.example.moviesappt.domain.model.ResultsItem
import com.example.moviesappt.domain.repo.MoviesRepo
import com.example.moviesappt.domain.utils.ResponseState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.lang.Exception
import javax.inject.Inject

class MoviesRepoImpl @Inject constructor (
    private val api: MoviesApi
) : MoviesRepo {
    override suspend fun getMovies(
        genersId: String,
        page: Int
    ): Flow<ResponseState<List<ResultsItem>>> {
        return flow {
            emit(ResponseState.Loading)
            try {
                val result = api.getMovies(
                    genersId = genersId,
                    page = page
                )
                if (result.isSuccessful) {
                    result.body()?.let {
                        val movieResponse = result.body()
                        val results = movieResponse?.results.orEmpty().filterNotNull()
                        emit(ResponseState.Success(results))
//                        emit(ResponseState.Success(it.results))
                    }
                } else {
                    emit(ResponseState.Failed(Exception(result.message())))
                }
            } catch (e: Exception) {
                emit(ResponseState.Failed(e))
            }
        }
    }
}