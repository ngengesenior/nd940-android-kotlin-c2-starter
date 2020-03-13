package com.udacity.asteroidradar.room

import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.BuildConfig
import com.udacity.asteroidradar.webservice.NasaAsteroidWebService
import timber.log.Timber

class Repository(val asteroidDao: AsteroidDao) {


    private val client:NasaAsteroidWebService
    get() = NasaAsteroidWebService.create()

    suspend fun getAsteroidsFromInternet() = client.getAsteroids(BuildConfig.API_KEY)

    suspend fun insertAsteroids(asteroids:ArrayList<Asteroid>) {
        for (asteroid in asteroids) {
            Timber.d("Saving to db $asteroid")
            asteroidDao.insertAsteroid(asteroid)
        }
    }

    // suspend fun getAsteroidsFromDb() = asteroidDao.getAllAsteroids()

    fun getAsteroidsLiveData() = asteroidDao.getAllAsteroids()
    //val asteroids:LiveData<List<Asteroid>> = asteroidDao.getAllAsteroids()

    suspend fun getImageOfTheDay() = client.getImageOfTheDay(BuildConfig.API_KEY)




}