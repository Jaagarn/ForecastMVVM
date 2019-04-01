package com.example.forecastmvvm.ui.data.network.data.response

import com.example.forecastmvvm.ui.data.db.entity.CurrentWeatherEntry
import com.example.forecastmvvm.ui.data.db.entity.Location
import com.google.gson.annotations.SerializedName

data class CurrentWeatherResponse(
    @SerializedName("current")
    val currentWeatherEntry: CurrentWeatherEntry,
    val location: Location
)