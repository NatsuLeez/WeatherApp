package com.example.weatherapp

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.DecimalFormat

class MainActivity : AppCompatActivity() {

    private lateinit var weatherApiClient: WeatherApiClient
    private lateinit var myTextView: TextView



    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val lat = 37.7749 // Replace with your latitude
        val lon = -122.4194 // Replace with your longitude
        val apiKey = "c15216721e16bfe86a214c4dec3f5abe"

        weatherApiClient = WeatherApiClient()
        myTextView = findViewById(R.id.textView)
        val f = DecimalFormat()
        f.maximumFractionDigits = 2

        // Example of making an API call
        val call = weatherApiClient.getWeatherData(lat, lon, apiKey)
        call.enqueue(object : Callback<WeatherData> {
            override fun onResponse(call: Call<WeatherData>, response: Response<WeatherData>) {
                if (response.isSuccessful) {
                    val weatherData = response.body()
                    if (weatherData != null) {
                        val temperature = weatherData.main.temp
                        val weatherAct = weatherData.weather[0].main
                        val weatherText = "Today there is $weatherAct"
                        val temperatureCels = temperature- 273.15
                        val temperatureText = "$weatherText with a temperature of ${f.format(temperatureCels)} °C\n"
                        myTextView.text = temperatureText
                    }
                } else {
                    // Handle error
                }
            }

            override fun onFailure(call: Call<WeatherData>, t: Throwable) {
                // Handle network error
            }
        })
    }

}