package com.example.hw1.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.example.hw1.R
import com.example.hw1.utilities.Constants
import com.example.hw1.utilities.SharedPreferencesManager

class MapFragment : Fragment(), OnMapReadyCallback {
    private var googleMap: GoogleMap? = null
    private var markers: MutableList<MarkerOptions> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_map, container, false)

        // Initialize the map
        val mapFragment = childFragmentManager.findFragmentById(R.id.map_view) as SupportMapFragment?
        mapFragment?.getMapAsync(this)

        return view
    }

    override fun onMapReady(map: GoogleMap) {
        this.googleMap = map

        val sharedPreferencesManager = SharedPreferencesManager.getInstance()
        val leaderboard = sharedPreferencesManager.getLeaderboard(Constants.Leaderboard.LEADERBOARD_KEY)

        // Clear existing markers
        markers.clear()

        // Add markers for all entries in leaderboard
        for (entry in leaderboard) {
            entry.latitude?.let { lat ->
                entry.longitude?.let { lng ->
                    val location = LatLng(lat, lng)
                    val markerOptions = MarkerOptions()
                        .position(location)
                        .title("${entry.name}: ${entry.score}")
                    markers.add(markerOptions)
                    map.addMarker(markerOptions)
                }
            }
        }

        // Set camera to last marker or default location
        val lastMarker = markers.lastOrNull()
        if (lastMarker != null) {
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(lastMarker.position, 10f))
        } else {
            val defaultLocation = LatLng(37.7749, -122.4194)
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultLocation, 10f))
        }
    }

    fun addMarker(latitude: Double, longitude: Double, title: String) {
        val location = LatLng(latitude, longitude)

        // Only add if marker doesn't exist at this location
        if (!markers.any { it.position.latitude == latitude && it.position.longitude == longitude }) {
            val markerOptions = MarkerOptions()
                .position(location)
                .title(title)
            markers.add(markerOptions)
            googleMap?.addMarker(markerOptions)
            googleMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15f))
        }
    }

    fun moveToLocation(latitude: Double, longitude: Double, title: String) {
        val location = LatLng(latitude, longitude)
        googleMap?.animateCamera(CameraUpdateFactory.newLatLngZoom(location, 15f))
    }
}