package com.example.forecastmvvm.ui.data.db.unitlocalized

class ImperialCurrentWeatherEntry (
    override val temperature: Double,
    override val conditionText: String,
    override val conditionIconUrl: String,
    override val windSpeed: Double,
    override val windDirection: String,
    override val precipitationVolume: Double,
    override val feelsLikeTemperature: Double,
    override val visibilityDistance: Double

) : UnitSpecifiedCurrentWeatherEntry