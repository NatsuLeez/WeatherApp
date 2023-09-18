package com.example.weatherapp

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class WeatherApiClient {
    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("https://api.openweathermap.org/data/2.5/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val apiService: WeatherApiService = retrofit.create(WeatherApiService::class.java)

    fun getWeatherData(lat: Double, lon: Double, apiKey: String): Call<WeatherData> {
        return apiService.getWeatherData(lat, lon, apiKey)
    }
}