package com.example.moviesappt.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.moviesappt.data.dao.MovieDao
import com.example.moviesappt.domain.model.ResultsItem
import com.example.moviesappt.domain.utils.ResponseState
import com.example.moviesapptask.domain.usecase.MoviesUseCase

class CacheInvalidationWorker(
    appContext: Context,
    workerParams: WorkerParameters,
    private val movieDao: MovieDao,
    private val useCase: MoviesUseCase
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        return try {
            // Delete all movies from the database
            movieDao.deleteAll()

            // Fetch fresh data from the API and store it in the database
            val response = useCase.invoke("28", 1)
            if (response is ResponseState.Success<*>) {
                val movies = (response as ResponseState.Success<List<*>>).data.map { movie ->
                    // Assuming you have a safe cast function for ResultsItem
                    val resultItem = movie as? ResultsItem
                    resultItem?.let {
                        ResultsItem(it.id.toString(), it.title)
                    }
                }.filterNotNull()
                movieDao.insertAll(movies)
            }

            Result.success()
        } catch (e: Exception) {
            Result.failure()
        }
    }
}