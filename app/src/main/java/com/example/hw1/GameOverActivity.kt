package com.example.hw1

import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.hw1.fragments.LeaderboardFragment
import com.example.hw1.fragments.MapFragment
import com.example.hw1.utilities.Constants
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.location.LocationRequest

class GameOverActivity : AppCompatActivity() {

    private var score : Int? = null
    private var name : String? = null
    private var validation : String? = null
    private var currentLatitude: Double? = null  // Add these to store location
    private var currentLongitude: Double? = null

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var mapFragment: MapFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gameover)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)


        init()
        checkLocationPermissions()
        loadMapFragment()
        loadLeaderboardFragment()
    }

    private fun init() {
        val bundle = intent.extras
        score = bundle?.getInt("score")
        name = bundle?.getString("name")
        validation = bundle?.getString("Validation")
    }

    private fun loadMapFragment() {
        mapFragment = MapFragment()
        val fragmentManager: FragmentManager = supportFragmentManager
        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()

        fragmentTransaction.replace(R.id.map_frame, mapFragment!!)
        fragmentTransaction.commit()
    }

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1001
    }


    private fun checkLocationPermissions() {
        if (ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
        } else {
            getCurrentLocation()
        }
    }

    private fun getCurrentLocation() {
        try {
            // Create location request
            val locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 5000)
                .setMinUpdateIntervalMillis(2000)
                .build()

            // Check permission again just to be safe
            if (ActivityCompat.checkSelfPermission(
                    this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                fusedLocationClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, null)
                    .addOnSuccessListener { location ->
                        if (location != null) {
                            val bundle = Bundle().apply {
                                putDouble("latitude", location.latitude)
                                putDouble("longitude", location.longitude)
                                currentLatitude = location.latitude
                                currentLongitude = location.longitude
                            }
                            mapFragment?.arguments = bundle
                            // Also update map directly if it's already loaded
                            Log.d("Location - getCurrentLocation", "Updating map with location: $currentLatitude $currentLongitude")
                            mapFragment?.addMarker(location.latitude, location.longitude, "$name: ${score ?: 0}")
                        } else {
                            // Handle null location
                            Log.d("Location", "Location is null")
                        }
                    }
                    .addOnFailureListener { e ->
                        Log.e("Location", "Error getting location", e)
                    }
            }
        } catch (e: SecurityException) {
            e.printStackTrace()
        }
    }

    private fun loadLeaderboardFragment() {
        val leaderboardFragment = LeaderboardFragment()
        val bundle = Bundle()
        bundle.putInt("score", score ?: 0)
        bundle.putString("name", name)
        bundle.putString("Validation", validation)
        currentLatitude?.let { bundle.putDouble("latitude", it) }
        currentLongitude?.let { bundle.putDouble("longitude", it) }
        leaderboardFragment.arguments = bundle
        Log.d("Location - loadLeaderboardFragment", "Latitude: $currentLatitude, Longitude: $currentLongitude")

        val fragmentManager: FragmentManager = supportFragmentManager
        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()


        fragmentTransaction.replace(R.id.leaderboard_frame, leaderboardFragment)
        fragmentTransaction.commit()
    }

}