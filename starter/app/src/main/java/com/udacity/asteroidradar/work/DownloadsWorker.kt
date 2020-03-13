package com.udacity.asteroidradar.work

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import com.udacity.asteroidradar.room.AppDatabase
import com.udacity.asteroidradar.room.Repository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject
import timber.log.Timber
import java.net.UnknownHostException
import kotlin.coroutines.CoroutineContext

class DownloadsWorker(appContext: Context, workParams: WorkerParameters) :
    CoroutineWorker(appContext, workParams) {

    private val repository: Repository

    init {
        Timber.d("Initialising Worker")
        val asteroidsDao = AppDatabase.getDatabase(appContext).asteroidDao()
        repository = Repository(asteroidsDao)
    }

    override suspend fun doWork(): Result {
        Timber.d("Doing work")
        // Download Nasa data and save to db

        var result: Result? = null

        val scope = CoroutineScope(Dispatchers.IO)


        try {
            val response = repository.getAsteroidsFromInternet()
            if (response.isSuccessful) {

                val responseString = response.body()!!
                val listOFAsteroids = parseAsteroidsJsonResult(JSONObject(responseString))
                repository.insertAsteroids(listOFAsteroids)
                return Result.success()


            } else {
                Timber.d("Response failed ${response.code()}")
                return Result.failure()

            }
        } catch (unKnHostException: UnknownHostException) {
            Timber.d("No internet")
            return Result.retry()
        } catch (exception: Exception) {
            Timber.d("Exception ${exception.message}")
            return Result.failure()
        }


    }
}