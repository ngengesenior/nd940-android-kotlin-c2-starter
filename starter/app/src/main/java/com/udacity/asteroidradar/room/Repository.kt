package com.udacity.asteroidradar.room

import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.webservice.NasaAsteroidWebService

class Repository(val asteroidDao: AsteroidDao) {


    private val client:NasaAsteroidWebService
    get() = NasaAsteroidWebService.create()

    suspend fun getAsteroidsFromInternet() = client.getAsteroids()

    suspend fun insertAsteroids(asteroids:ArrayList<Asteroid>) {
        for (asteroid in asteroids) {
            asteroidDao.insertAsteroid(asteroid)
        }
    }

    // suspend fun getAsteroidsFromDb() = asteroidDao.getAllAsteroids()

    fun getAsteroidsLiveData() = asteroidDao.getAllAsteroids()
    //val asteroids:LiveData<List<Asteroid>> = asteroidDao.getAllAsteroids()

    suspend fun getImageOfTheDay() = client.getImageOfTheDay()




}