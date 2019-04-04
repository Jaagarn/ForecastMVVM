package com.example.forecastmvvm.ui.data.repository

import androidx.lifecycle.LiveData
import com.example.forecastmvvm.ui.data.db.entity.WeatherLocation
import com.example.forecastmvvm.ui.data.db.unitlocalized.current.UnitSpecifiedCurrentWeatherEntry
import com.example.forecastmvvm.ui.data.db.unitlocalized.future.detail.UnitSpecificDetailFutureWeatherEntry
import com.example.forecastmvvm.ui.data.db.unitlocalized.future.list.UnitSpecificSimpleFutureWeatherEntry
import org.threeten.bp.LocalDate


interface ForecastRepository {
    suspend fun getCurrentWeather(metric: Boolean): LiveData<out UnitSpecifiedCurrentWeatherEntry>

    suspend fun getFutureWeatherList(startDate: LocalDate, metric: Boolean):LiveData<out List<UnitSpecificSimpleFutureWeatherEntry>>

    suspend fun getFutureWeatherByDate(date: LocalDate, metric: Boolean):LiveData<out List<UnitSpecificDetailFutureWeatherEntry>>

    suspend fun getWeatherLocation(): LiveData<WeatherLocation>
}