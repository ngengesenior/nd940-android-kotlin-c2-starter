package com.udacity.asteroidradar.main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.PictureOfDay
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import com.udacity.asteroidradar.room.AppDatabase
import com.udacity.asteroidradar.room.Repository
import com.udacity.asteroidradar.webservice.NasaAsteroidWebService
import kotlinx.coroutines.*
import org.json.JSONObject
import timber.log.Timber
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class MainViewModel(application: Application) : AndroidViewModel(application) {
    val repository:Repository
    val database:AppDatabase? = null
    val webService = NasaAsteroidWebService.create()

    val _allAsteroids =  MutableLiveData<List<Asteroid>>()
    val job = Job()
    var uiscope = CoroutineScope(Dispatchers.Main + job)

    var imageOfTheDay= MutableLiveData<PictureOfDay>()


    val allAsteroids: LiveData<List<Asteroid>>
        get() = _allAsteroids

    init {
        val database = AppDatabase.getDatabase(application)
        val dao = database.asteroidDao()
        repository = Repository(dao)

        getUpdatedAsteriods()
        getImageOfTheDay()
    }

    fun getUpdatedAsteriods() {
        uiscope.launch {
            try {
                val res = getNewAsteroids()

                if(res != null) {
                    _allAsteroids.value = res
                } else {
                    //Get locally stored asterions
                    //_allAsteroids.value = allAsteroids

                    _allAsteroids.value = repository.getAsteroidsLiveData().value

                }
            } catch (e: Exception) {
                _allAsteroids.value = repository.getAsteroidsLiveData().value
                Timber.d("Generic error ${e.message} ${repository.getAsteroidsLiveData().value}")
            }
        }
        }

    fun getImageOfTheDay() {

            try {
                uiscope.launch {

                    val imageResponse = repository.getImageOfTheDay()
                    if (imageResponse.isSuccessful) {
                        imageOfTheDay.value = imageResponse.body()!!
                    } else {

                    }
                }

            }catch (unkHost:UnknownHostException){

            } catch (socketTO:SocketTimeoutException) {
                Timber.d("Timed out ${socketTO.message}")
            }

            catch (ex:Exception) {

            }

    }
    suspend fun getNewAsteroids():List<Asteroid>? {
        return withContext(Dispatchers.IO) {
            try {
                val response = webService.getAsteroids()

                if (response.isSuccessful){
                    return@withContext parseAsteroidsJsonResult(JSONObject(response.body()))
                } else return@withContext  null
            } catch (ex:UnknownHostException) {
                Timber.d("No internet")
                return@withContext null
            }

            catch (connectEx:SocketTimeoutException) {
                Timber.d("Socket time out ${connectEx.message}")
                return@withContext null
            }

            catch (ex:Exception) {
                Timber.d("Exception ${ex.message}")
                return@withContext null
            }

        }

    }

    }

