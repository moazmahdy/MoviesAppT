package com.example.moviesappt.di

import com.example.moviesappt.data.remote.MoviesApi
import com.example.moviesappt.data.repoimpl.MoviesRepoImpl
import com.example.moviesappt.domain.repo.MoviesRepo
import com.example.moviesapptask.domain.usecase.MoviesUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MoviesModule {

    @Provides
    @Singleton
    fun provideMoviesRepo(
        api: MoviesApi
    ): MoviesRepo {
        return MoviesRepoImpl(
            api = api
        )
    }

    @Provides
    @Singleton
    fun provideMoviesUseCase(
        repo: MoviesRepo
    ): MoviesUseCase {
        return MoviesUseCase(repo = repo)
    }
}