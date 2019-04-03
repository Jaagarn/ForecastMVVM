package com.example.forecastmvvm.ui.data.network.response

import com.example.forecastmvvm.ui.data.db.entity.FutureWeatherEntry
import com.google.gson.annotations.SerializedName

data class ForecastDaysContainer(
    @SerializedName("forecastday")
    val entries: List<FutureWeatherEntry>
)