package com.example.forecastmvvm.ui.data.repository

import androidx.lifecycle.LiveData
import com.example.forecastmvvm.ui.data.db.unitlocalized.UnitSpecifiedCurrentWeatherEntry

interface ForecastRepository {
    suspend fun getCurrentWeather(metric: Boolean): LiveData<out UnitSpecifiedCurrentWeatherEntry>
}