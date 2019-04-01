package com.example.forecastmvvm.ui.data

import com.example.forecastmvvm.ui.data.network.data.response.CurrentWeatherResponse
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import kotlinx.coroutines.Deferred
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query


const val API_KEY = "f5e3d15da2f14d1a9f971156190104"

//http://api.apixu.com/v1/current.json?key=f5e3d15da2f14d1a9f971156190104&q=London

interface ApixuWeatherApiService {
    @GET("current.json")
    fun getCurrentWeather(
        @Query("q") location: String,
        @Query("lang")languageCode: String="en"
    ): Deferred<CurrentWeatherResponse>

    companion object{
        operator fun invoke(): ApixuWeatherApiService{
            val requestInterceptor = Interceptor { chain ->

                val url = chain.request().url().newBuilder().addQueryParameter("key", API_KEY).build()
                val request = chain.request().newBuilder().url(url).build()

                return@Interceptor chain.proceed(request)
            }

            val okHttpClient = OkHttpClient.Builder().addInterceptor(requestInterceptor).build()
            return Retrofit.Builder().client(okHttpClient).baseUrl("http://api.apixu.com/v1/")
                .addCallAdapterFactory(CoroutineCallAdapterFactory()).addConverterFactory(GsonConverterFactory.create())
                .build().create(ApixuWeatherApiService::class.java)
        }
    }
}