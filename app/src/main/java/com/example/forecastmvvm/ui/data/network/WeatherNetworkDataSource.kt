package com.example.forecastmvvm.ui.data.network

import androidx.lifecycle.LiveData
import com.example.forecastmvvm.ui.data.network.response.CurrentWeatherResponse

interface WeatherNetworkDataSource {
    val downloadedCurrentWeather: LiveData<CurrentWeatherResponse>

    suspend fun fetchCurrentWeather(
        location: String,
        languageCode: String
    )
}