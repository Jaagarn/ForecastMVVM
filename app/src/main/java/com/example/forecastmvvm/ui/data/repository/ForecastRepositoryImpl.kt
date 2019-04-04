package com.example.forecastmvvm.ui.data.repository

import androidx.lifecycle.LiveData
import com.example.forecastmvvm.ui.data.db.CurrentWeatherDAO
import com.example.forecastmvvm.ui.data.db.FutureWeatherDao
import com.example.forecastmvvm.ui.data.db.WeatherLocationDao
import com.example.forecastmvvm.ui.data.db.entity.WeatherLocation
import com.example.forecastmvvm.ui.data.db.unitlocalized.current.UnitSpecifiedCurrentWeatherEntry
import com.example.forecastmvvm.ui.data.db.unitlocalized.future.detail.UnitSpecificDetailFutureWeatherEntry
import com.example.forecastmvvm.ui.data.db.unitlocalized.future.list.UnitSpecificSimpleFutureWeatherEntry
import com.example.forecastmvvm.ui.data.network.FORECAST_DAYS
import com.example.forecastmvvm.ui.data.network.WeatherNetworkDataSource
import com.example.forecastmvvm.ui.data.network.response.CurrentWeatherResponse
import com.example.forecastmvvm.ui.data.network.response.FutureWeatherResponse
import com.example.forecastmvvm.ui.data.provider.LocationProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.threeten.bp.LocalDate
import java.time.ZonedDateTime
//import org.threeten.bp.ZonedDateTime


import java.util.*

class ForecastRepositoryImpl (
    private val currentWeatherDAO: CurrentWeatherDAO,
    private val futureWeatherDao: FutureWeatherDao,
    private val weatherLocationDao: WeatherLocationDao,
    private val weatherNetworkDataSource: WeatherNetworkDataSource,
    private val locationProvider: LocationProvider
): ForecastRepository {

    init {
        weatherNetworkDataSource.apply {
            downloadedCurrentWeather.observeForever { newCurrentWeather ->
                persistFetchedCurrentWeather(newCurrentWeather)
            }
            downloadedFutureWeather.observeForever { newFutureWeather ->
                persistFetchedFutureWeather(newFutureWeather)
            }
        }
    }

    override suspend fun getCurrentWeather(metric: Boolean): LiveData<out UnitSpecifiedCurrentWeatherEntry> {
        return withContext(Dispatchers.IO){
            initWeatherData()
            return@withContext if (metric) currentWeatherDAO.getMetric()
            else currentWeatherDAO.getImperial()
        }
    }

    override suspend fun getFutureWeatherList(
        startDate: LocalDate,
        metric: Boolean
    ): LiveData<out List<UnitSpecificSimpleFutureWeatherEntry>> {
        return withContext(Dispatchers.IO){
            initWeatherData()
            return@withContext if(metric)futureWeatherDao.getSimpleWeatherForecastsMetric(startDate)
            else futureWeatherDao.getSimpleWeatherForecastsImperial(startDate)
        }
    }

    override suspend fun getFutureWeatherByDate(
        date: LocalDate,
        metric: Boolean
    ): LiveData<out List<UnitSpecificDetailFutureWeatherEntry>> {
        return withContext(Dispatchers.IO){
            initWeatherData()
            return@withContext if(metric)futureWeatherDao.getDetailedWeatherForecastsMetric(date)
            else futureWeatherDao.getDetailedWeatherForecastsImperial(date)
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

    private fun persistFetchedFutureWeather(fetchedWeather:FutureWeatherResponse){
        fun deleteOldForecastData(){
            val today = LocalDate.now()
            futureWeatherDao.deleteOldEntries(today)
        }
        GlobalScope.launch(Dispatchers.IO) {
            deleteOldForecastData()
            val futureWeatherList=fetchedWeather.futureWeatherEntries.entries
            futureWeatherDao.insert(futureWeatherList)
            weatherLocationDao.upsert(fetchedWeather.location)
        }
    }

    private suspend fun initWeatherData(){

        val lastWeatherLocation = weatherLocationDao.getLocationNonLive()

        if(lastWeatherLocation==null
            || locationProvider.hasLocationChanged(lastWeatherLocation)) {
            fetchCurrentWeather()
            fetchFutureWeather()
            return
        }

        if (isFetchCurrentNeeded(lastWeatherLocation.zonedDateTime))
            fetchCurrentWeather()

        if(isFetchFutureNeeded())
            fetchFutureWeather()
    }

    private suspend fun fetchCurrentWeather(){
        weatherNetworkDataSource.fetchCurrentWeather(
            locationProvider.getPreferredLocationString(),
            Locale.getDefault().language
        )
    }
    private suspend fun fetchFutureWeather(){
        weatherNetworkDataSource.fetchFutureWeather(
            locationProvider.getPreferredLocationString(),
            Locale.getDefault().language
        )
    }


    private fun isFetchCurrentNeeded(lastFetchTime: ZonedDateTime): Boolean{
        val thirtyMinutesAgo = ZonedDateTime.now().minusMinutes(30)
        return true//lastFetchTime.isBefore(thirtyMinutesAgo)
    }

    private fun isFetchFutureNeeded():Boolean{
        val today = LocalDate.now()
        val futureWeatherCount= futureWeatherDao.countFutureWeather(today)
        return futureWeatherCount< FORECAST_DAYS
    }
}