package com.example.forecastmvvm.ui.data.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.forecastmvvm.ui.data.db.entity.CURRENT_WEATHER_ID
import com.example.forecastmvvm.ui.data.db.unitlocalized.ImperialCurrentWeatherEntry
import com.example.forecastmvvm.ui.data.db.unitlocalized.MetricCurrentWeatherEntry
import com.example.forecastmvvm.ui.data.db.unitlocalized.UnitSpecifiedCurrentWeatherEntry

@Dao
interface CurrentWeatherDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun upsert(weatherEntry: UnitSpecifiedCurrentWeatherEntry)
    @Query("select * from current_Weather where id=$CURRENT_WEATHER_ID")
    fun getMetric(): LiveData<MetricCurrentWeatherEntry>
    @Query("select * from current_Weather where id=$CURRENT_WEATHER_ID")
    fun getImperial(): LiveData<ImperialCurrentWeatherEntry>

}