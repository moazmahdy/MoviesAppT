package com.example.moviesapptask.domain.usecase

import com.example.moviesappt.domain.repo.MoviesRepo

class MoviesUseCase(
    private val repo: MoviesRepo
) {

    suspend operator fun invoke(
        genersId: String,
        page: Int
    ) = repo.getMovies(
        genersId = genersId,
        page = page
    )
}