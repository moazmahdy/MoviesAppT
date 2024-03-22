package com.example.moviesappt

import android.app.Application
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.moviesappt.worker.CacheInvalidationWorker
import dagger.hilt.android.HiltAndroidApp
import java.util.concurrent.TimeUnit

@HiltAndroidApp
class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        schedulePeriodicCacheInvalidation()
    }

    private fun schedulePeriodicCacheInvalidation() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val repeatingRequest = PeriodicWorkRequestBuilder<CacheInvalidationWorker>(
            4, TimeUnit.HOURS
        ).setConstraints(constraints)
            .build()

        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            "cache_invalidation_worker",
            ExistingPeriodicWorkPolicy.REPLACE,
            repeatingRequest
        )
    }
}