package com.example.forecastmvvm.ui.ui.settings.weather.current

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer

import com.example.forecastmvvm.R
import com.example.forecastmvvm.ui.data.network.ApixuWeatherApiService
import com.example.forecastmvvm.ui.data.network.ConnectivityInterceptorImpl
import com.example.forecastmvvm.ui.data.network.WeatherNetworkDataSourceImpl
import com.example.forecastmvvm.ui.internal.glide.ForecastAppGlideModule
import com.example.forecastmvvm.ui.internal.glide.GlideApp
import com.example.forecastmvvm.ui.ui.settings.base.ScopedFragment
import kotlinx.android.synthetic.main.current_weather_fragment.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance

class CurrentWeatherFragment : ScopedFragment(),KodeinAware {
    override val kodein by closestKodein()
    private val viewModelFactory: CurrentWeatherViewModelFactory by instance()



    private lateinit var viewModel: CurrentWeatherViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.current_weather_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this,viewModelFactory).get(CurrentWeatherViewModel::class.java)

        bindUI()


    }

    private fun bindUI() = launch{
        val currentWeather = viewModel.weather.await()

        val weatherLocation = viewModel.weatherLocation.await()

        weatherLocation.observe(this@CurrentWeatherFragment, Observer { location ->
            if(location==null) return@Observer
            updateLocation(location.name)
        })

        currentWeather.observe(this@CurrentWeatherFragment, Observer {
            if(it == null) return@Observer

            group_loading.visibility=View.GONE

            updateDateToToday()
            updateTemperatures(it.temperature,it.feelsLikeTemperature)
            updateCondition(it.conditionText)
            updatePrecipitation(it.precipitationVolume)
            updateWind(it.windDirection,it.windSpeed)
            updateVisibility(it.visibilityDistance)

            GlideApp.with(this@CurrentWeatherFragment)
                .load("http:${it.conditionIconUrl}")
                .into(imageView_condition_icon)
        })
    }

    private fun chooseLocalizedUnitAbbreviation(metric: String, imperial: String): String{
        return if(viewModel.isMetric) metric else imperial
    }

    private fun updateLocation(location: String){
        (activity as? AppCompatActivity)?.supportActionBar?.title = location
    }

    private fun updateDateToToday(){
        (activity as? AppCompatActivity)?.supportActionBar?.subtitle = "Today"
    }

    private fun updateTemperatures(temperature:Double,feelsLike:Double){
        val unitAbbriviation = chooseLocalizedUnitAbbreviation("°C" ,"°F")
        textView_temperature.text ="$temperature$unitAbbriviation"
        textView_feels_like_temperature.text = "Feels like $feelsLike$unitAbbriviation"
    }

    private fun updateCondition(condition: String){
        textView_condition.text = condition
    }

    private fun updatePrecipitation(precipitationVolume:Double){
        val unitAbbreviation = chooseLocalizedUnitAbbreviation("mm" , "in")
        textView_precipitation.text="Preciptiation: $precipitationVolume $unitAbbreviation"
    }

    private fun updateWind(windDirection: String, windSpeed: Double) {
        val unitAbbriviation = chooseLocalizedUnitAbbreviation("kph", "mph")
        textView_wind.text ="Wind: $windDirection, $windSpeed $unitAbbriviation"
    }

    private fun updateVisibility(visibilityDistance: Double) {
        val unitAbbriviation = chooseLocalizedUnitAbbreviation("km", "miles")
        textView_visibility.text ="Visibility: $visibilityDistance $unitAbbriviation"
    }
}
