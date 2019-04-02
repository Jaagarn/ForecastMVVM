package com.example.forecastmvvm.ui.ui.settings.weather.current

import androidx.lifecycle.ViewModel;
import com.example.forecastmvvm.ui.data.repository.ForecastRepository
import com.example.forecastmvvm.ui.internal.UnitSystem
import com.example.forecastmvvm.ui.internal.lazyDeferred

class CurrentWeatherViewModel(
    private val forecastRepository: ForecastRepository
) : ViewModel(){
    private val unitSystem = UnitSystem.METRIC//Get from settings later
    val isMetric: Boolean
        get() = unitSystem == UnitSystem.METRIC

    val weather by lazyDeferred() {
        forecastRepository.getCurrentWeather(isMetric)
    }
}
