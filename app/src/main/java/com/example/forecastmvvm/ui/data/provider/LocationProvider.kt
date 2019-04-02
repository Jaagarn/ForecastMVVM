package com.example.forecastmvvm.ui.data.provider

import com.example.forecastmvvm.ui.data.db.entity.WeatherLocation

interface LocationProvider {
    suspend fun hasLocationChanged(lasWeatherLocation: WeatherLocation):Boolean
    suspend fun getPreferredLocationString():String
}