package com.udacity.asteroidradar

import android.app.Application
import android.content.Context
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.udacity.asteroidradar.work.DownloadsWorker
import timber.log.Timber
import java.util.concurrent.TimeUnit

class App : Application() {
    override fun onCreate() {

        Timber.plant(Timber.DebugTree())

        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .setRequiresCharging(true)
            .build()

        val downloadRequest = PeriodicWorkRequestBuilder<DownloadsWorker>(24, TimeUnit.HOURS)
            .setConstraints(constraints)
            .build()
            WorkManager.getInstance(this)
                .enqueue(downloadRequest)

        super.onCreate()
    }
}