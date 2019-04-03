package com.example.forecastmvvm.ui.data.network.response

import com.google.gson.annotations.SerializedName

data class ForecastDaysContainer(
    @SerializedName("forecastday")
    val entries: List<Forecastday>
)