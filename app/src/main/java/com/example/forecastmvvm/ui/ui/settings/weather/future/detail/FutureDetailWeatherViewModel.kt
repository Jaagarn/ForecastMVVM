package com.example.forecastmvvm.ui.ui.settings.weather.future.detail

import com.example.forecastmvvm.ui.data.provider.UnitProvider
import com.example.forecastmvvm.ui.data.repository.ForecastRepository
import com.example.forecastmvvm.ui.internal.lazyDeferred
import com.example.forecastmvvm.ui.ui.settings.base.WeatherViewModel
import org.threeten.bp.LocalDate


class FutureDetailWeatherViewModel (
    private val detailDate: LocalDate,
    private val forecastRepository: ForecastRepository,
    unitProvider: UnitProvider

): WeatherViewModel(forecastRepository , unitProvider) {

    val weather by lazyDeferred{
        forecastRepository.getFutureWeatherByDate(detailDate, super.isMetricUnit)
    }
}
