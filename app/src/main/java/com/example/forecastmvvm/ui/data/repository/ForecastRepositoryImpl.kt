package com.example.forecastmvvm.ui.data.repository

import androidx.lifecycle.LiveData
import com.example.forecastmvvm.ui.data.db.CurrentWeatherDAO
import com.example.forecastmvvm.ui.data.db.WeatherLocationDao
import com.example.forecastmvvm.ui.data.db.entity.WeatherLocation
import com.example.forecastmvvm.ui.data.db.unitlocalized.UnitSpecifiedCurrentWeatherEntry
import com.example.forecastmvvm.ui.data.network.WeatherNetworkDataSource
import com.example.forecastmvvm.ui.data.network.response.CurrentWeatherResponse
import com.example.forecastmvvm.ui.data.provider.LocationProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.ZonedDateTime
import java.util.*

class ForecastRepositoryImpl (
    private val currentWeatherDAO: CurrentWeatherDAO,
    private val weatherLocationDao: WeatherLocationDao,
    private val weatherNetworkDataSource: WeatherNetworkDataSource,
    private val locationProvider: LocationProvider
): ForecastRepository {

    init {
        weatherNetworkDataSource.downloadedCurrentWeather.observeForever { newCurrentWeather ->
            persistFetchedCurrentWeather(newCurrentWeather)
        }
    }

    override suspend fun getCurrentWeather(metric: Boolean): LiveData<out UnitSpecifiedCurrentWeatherEntry> {
        return withContext(Dispatchers.IO){
            initWeatherData()
            return@withContext if (metric) currentWeatherDAO.getMetric()
            else currentWeatherDAO.getImperial()
        }
    }

    override suspend fun getWeatherLocation(): LiveData<WeatherLocation>{
        return withContext(Dispatchers.IO){
            return@withContext weatherLocationDao.getLocation()
        }
    }

    private fun persistFetchedCurrentWeather(fetchedWeather: CurrentWeatherResponse){
        GlobalScope.launch(Dispatchers.IO) {
            currentWeatherDAO.upsert(fetchedWeather.currentWeatherEntry)
            weatherLocationDao.upsert(fetchedWeather.location)
        }
    }

    private suspend fun initWeatherData(){

        val lastWeatherLocation = weatherLocationDao.getLocation().value

        if(lastWeatherLocation==null
            || locationProvider.hasLocationChanged(lastWeatherLocation)) {
            fetchCurrentWeather()
            return
        }

        if (isFetchCurrentNeeded(lastWeatherLocation.zonedDateTime))
            fetchCurrentWeather()
    }

    private suspend fun fetchCurrentWeather(){
        weatherNetworkDataSource.fetchCurrentWeather(
            locationProvider.getPreferredLocationString(),
            Locale.getDefault().language
        )
    }

    private fun  isFetchCurrentNeeded(lastFetchTime: ZonedDateTime): Boolean{
        val thirtyMinutesAgo = ZonedDateTime.now().minusMinutes(30)
        return lastFetchTime.isBefore(thirtyMinutesAgo)
    }
}