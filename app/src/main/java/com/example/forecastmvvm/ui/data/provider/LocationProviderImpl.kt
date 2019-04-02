package com.example.forecastmvvm.ui.data.provider

import com.example.forecastmvvm.ui.data.db.entity.WeatherLocation

class LocationProviderImpl : LocationProvider {
    override suspend fun hasLocationChanged(lasWeatherLocation: WeatherLocation): Boolean {
        return true
    }

    override suspend fun getPreferredLocationString(): String {
        return "Los Angeles"
    }
}