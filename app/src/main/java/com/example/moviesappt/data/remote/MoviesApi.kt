package com.example.moviesappt.data.remote

import com.example.moviesappt.domain.model.MovieResponse
import com.example.moviesappt.domain.model.ResultsItem
import com.example.moviesappt.domain.utils.Constants
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface MoviesApi {

    @GET("discover/movie")
    suspend fun getMovies(
        @Query("api_key") apiKey: String = Constants.API_KEY,
        @Query("with_genres") genersId: String,
        @Query("page") page: Int,
        @Query("language") language: String = "en-US",
    ): Response<MovieResponse>
}