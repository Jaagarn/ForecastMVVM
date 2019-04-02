package com.example.forecastmvvm.ui.data.network.response

import com.example.forecastmvvm.ui.data.db.entity.CurrentWeatherEntry
import com.example.forecastmvvm.ui.data.db.entity.WeatherLocation
import com.google.gson.annotations.SerializedName

data class CurrentWeatherResponse(
    @SerializedName("current")
    val currentWeatherEntry: CurrentWeatherEntry,
    val location: WeatherLocation
)