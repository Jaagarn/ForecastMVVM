package com.example.forecastmvvm.data.network.response

import com.google.gson.annotations.SerializedName

data class ForecastDaysContainer(
    @SerializedName("forecastday")
    val entries: List<FutureWeatherResponse>
)