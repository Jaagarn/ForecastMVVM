package com.example.forecastmvvm.ui.ui.settings.weather.future.list

import androidx.lifecycle.ViewModel;
import com.example.forecastmvvm.ui.data.provider.UnitProvider
import com.example.forecastmvvm.ui.data.repository.ForecastRepository
import com.example.forecastmvvm.ui.internal.lazyDeferred
import com.example.forecastmvvm.ui.ui.settings.base.WeatherViewModel
import org.threeten.bp.LocalDate


class FutureListWeatherViewModel (
    private val forecastRepository: ForecastRepository,
    unitProvider: UnitProvider
): WeatherViewModel(forecastRepository,unitProvider) {

    val weatherEntries by lazyDeferred {
        forecastRepository.getFutureWeatherList(LocalDate.now(),super.isMetricUnit)
    }
}
