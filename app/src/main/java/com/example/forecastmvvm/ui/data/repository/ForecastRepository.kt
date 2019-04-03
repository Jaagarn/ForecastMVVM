package com.example.forecastmvvm.ui.data.repository

import androidx.lifecycle.LiveData
import com.example.forecastmvvm.ui.data.db.entity.WeatherLocation
import com.example.forecastmvvm.ui.data.db.unitlocalized.current.UnitSpecifiedCurrentWeatherEntry
import com.example.forecastmvvm.ui.data.db.unitlocalized.future.UnitSpecificSimpleFutureWeatherEntry
import org.threeten.bp.LocalDate


interface ForecastRepository {
    suspend fun getCurrentWeather(metric: Boolean): LiveData<out UnitSpecifiedCurrentWeatherEntry>

    suspend fun getFutureWeatherList(startDate: LocalDate, metric: Boolean):LiveData<out List<UnitSpecificSimpleFutureWeatherEntry>>

    suspend fun getWeatherLocation(): LiveData<WeatherLocation>
}