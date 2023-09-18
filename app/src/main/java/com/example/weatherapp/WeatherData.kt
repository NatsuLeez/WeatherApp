package com.example.weatherapp

data class WeatherData (
    val coord: Coord,
    val weather: List<Weather>,
    val main: Main,
    val wind: Wind
)

data class Coord(
    val lon: Double,
    val lat: Double
)

data class Weather(
    val id: Int,
    val main: String,
    val description: String,
    val icon: String
)

data class Main(
    val temp: Double,
    val temp_min: Double,
    val temp_max: Double,
    val pressure: Int,
    val humidity: Int
)

data class Wind(
    val speed: Double,
    val deg: Int,
    val gust: Double
)
