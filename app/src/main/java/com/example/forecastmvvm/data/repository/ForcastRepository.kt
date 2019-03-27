package com.example.forecastmvvm.data.repository

import androidx.lifecycle.LiveData
import com.example.forecastmvvm.data.db.unitlocalized.UnitSpecificCurrentWeatherEntry

interface ForcastRepository {
    suspend fun getCurrentWeather(metric: Boolean): LiveData<out UnitSpecificCurrentWeatherEntry>
}