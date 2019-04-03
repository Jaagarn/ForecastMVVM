package com.example.forecastmvvm.ui.data.network.response

import com.google.gson.annotations.SerializedName

data class FutureWeatherResponse(
    val current: Current,
    val forecast: Forecast,
    val location: Location
)