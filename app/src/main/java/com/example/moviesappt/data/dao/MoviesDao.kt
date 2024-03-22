package com.example.moviesappt.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.moviesappt.domain.model.ResultsItem
import kotlinx.coroutines.flow.Flow

@Dao
interface MovieDao {
    @Query("SELECT * FROM movie")
    fun getMovies(): Flow<List<ResultsItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(movies: List<ResultsItem>)

    @Query("DELETE FROM movie")
    suspend fun deleteAll()
}