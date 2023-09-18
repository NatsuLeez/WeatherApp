package com.example.weatherapp

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.CameraUpdateFactory
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.DecimalFormat
import android.Manifest
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions;
import kotlin.properties.Delegates

internal class MainActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var weatherApiClient: WeatherApiClient
    private lateinit var myTextView: TextView
    private lateinit var mMap: GoogleMap
    private var lat: Double = DEFAULT_LATITUDE
    private var lon: Double = DEFAULT_LONGITUDE
    private var currentMarker: Marker? = null
    private lateinit var call: Call<WeatherData>


    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)



        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager

        lat = DEFAULT_LATITUDE
        lon = DEFAULT_LONGITUDE


        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            // Vous avez la permission de localisation
        } else {
            // Demandez la permission de localisation
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1)
        }

        val locationListener = object : LocationListener {
            override fun onLocationChanged(location: Location) {
                // Vous pouvez obtenir la latitude et la longitude à partir de l'objet Location
                lat = location.latitude
                lon = location.longitude

                // Faites quelque chose avec les données de localisation
            }

            @Deprecated("Deprecated in Java")
            override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}

            override fun onProviderEnabled(provider: String) {}

            override fun onProviderDisabled(provider: String) {}
        }



        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0f, locationListener)

        weatherApiClient = WeatherApiClient()
        myTextView = findViewById(R.id.textView)





    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        val defaultCoordonnee = LatLng(lat, lon)

        val apiKey = "c15216721e16bfe86a214c4dec3f5abe"

        val f = DecimalFormat()
        f.maximumFractionDigits = 2

        val markerOptions = MarkerOptions()
            .position(defaultCoordonnee)
            .title("Position par défaut")
        currentMarker = googleMap.addMarker(markerOptions)
        mMap.moveCamera(CameraUpdateFactory.newLatLng(defaultCoordonnee))

        googleMap.setOnMapClickListener { latLng ->
            currentMarker?.remove()
            lat = latLng.latitude
            lon = latLng.longitude
            val newCoordonnee = LatLng(lat, lon)
            val markerOptions = MarkerOptions()
                .position(latLng)
                .title("Clic sur la carte")
            currentMarker = googleMap.addMarker(markerOptions)
            mMap.moveCamera(CameraUpdateFactory.newLatLng(newCoordonnee))

            call = weatherApiClient.getWeatherData(lat, lon, apiKey)
            call.enqueue(object : Callback<WeatherData> {
                override fun onResponse(call: Call<WeatherData>, response: Response<WeatherData>) {
                    if (response.isSuccessful) {
                        val weatherData = response.body()
                        if (weatherData != null) {
                            val temperature = weatherData.main.temp
                            val descWeatherAct = weatherData.weather[0].description
                            val humidityAct = weatherData.main.humidity
                            val windSpeed = weatherData.wind.speed
                            val weatherText = "Today there is a $descWeatherAct the humidity is $humidityAct %, the wind is $windSpeed m/s"
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

    companion object {
        private const val DEFAULT_LATITUDE = 0.0 // Latitude par défaut
        private const val DEFAULT_LONGITUDE = 0.0 // Longitude par défaut
    }
}