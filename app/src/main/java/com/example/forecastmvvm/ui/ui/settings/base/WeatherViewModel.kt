package com.example.forecastmvvm.ui.ui.settings.base

import androidx.lifecycle.ViewModel
import com.example.forecastmvvm.ui.data.provider.UnitProvider
import com.example.forecastmvvm.ui.data.repository.ForecastRepository
import com.example.forecastmvvm.ui.internal.UnitSystem
import com.example.forecastmvvm.ui.internal.lazyDeferred

abstract class WeatherViewModel (
    private val forecastRepository: ForecastRepository,
    unitProvider: UnitProvider
): ViewModel(){
    private val unitSystem = unitProvider.getUnitSystem()

    val isMetricUnit:Boolean
        get()=unitSystem==UnitSystem.METRIC

    val weatherLocation by lazyDeferred {
        forecastRepository.getWeatherLocation()
    }
}