package com.example.forecastmvvm.ui.ui.settings.weather.current

import androidx.lifecycle.ViewModel;
import com.example.forecastmvvm.ui.data.provider.UnitProvider
import com.example.forecastmvvm.ui.data.repository.ForecastRepository
import com.example.forecastmvvm.ui.internal.UnitSystem
import com.example.forecastmvvm.ui.internal.lazyDeferred
import com.example.forecastmvvm.ui.ui.settings.base.WeatherViewModel

class CurrentWeatherViewModel(
    private val forecastRepository: ForecastRepository,
    unitProvider: UnitProvider
) : WeatherViewModel(forecastRepository,unitProvider){


    val weather by lazyDeferred() {
        forecastRepository.getCurrentWeather(super.isMetricUnit)
    }

}
