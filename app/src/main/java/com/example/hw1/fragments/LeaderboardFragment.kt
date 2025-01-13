package com.example.hw1.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.hw1.R
import com.example.hw1.ScoreAdapter
import com.example.hw1.utilities.Constants
import com.example.hw1.utilities.LeaderboardEntry
import com.example.hw1.utilities.SharedPreferencesManager
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class LeaderboardFragment : Fragment() {

    private lateinit var leaderboardRecyclerView: RecyclerView
    private var sharedPreferencesManager: SharedPreferencesManager = SharedPreferencesManager.getInstance()
    private val leaderboard = sharedPreferencesManager.getLeaderboard(Constants.Leaderboard.LEADERBOARD_KEY).toMutableList()
    private var currentLatitude: Double? = null
    private var currentLongitude: Double? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            currentLatitude = it.getDouble("latitude")
            currentLongitude = it.getDouble("longitude")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_leaderboard, container, false)
        leaderboardRecyclerView = view.findViewById(R.id.leaderboard_RV_list)


        val name = arguments?.getString("name") ?: "Unknown"
        val score = arguments?.getInt("score") ?: 0
        val validation = arguments?.getString("Validation")

        if(validation == Constants.Leaderboard.VALID_KEY)
            updateLeaderboard(name, score)

        setupRecyclerView(leaderboard)
        return view
    }

    private fun updateLeaderboard(name: String, score: Int) {
        val currentDate = getCurrentDate()

        // Create new entry with location
        val newEntry = LeaderboardEntry(
            name = name,
            score = score,
            date = currentDate,
            latitude = currentLatitude,
            longitude = currentLongitude
        )

        // Log for debugging
        Log.d("Location - updateLeaderboard", "Saving entry with lat=${newEntry.latitude}, lng=${newEntry.longitude}")

        leaderboard.add(newEntry)
        leaderboard.sortByDescending { it.score }

        // Save updated leaderboard
        sharedPreferencesManager.putLeaderboard(Constants.Leaderboard.LEADERBOARD_KEY, leaderboard)

    }

    // Helper function to get the current date
    private fun getCurrentDate(): String {
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        return dateFormat.format(Date())
    }

    private fun setupRecyclerView(leaderboard: List<LeaderboardEntry>) {
        val scoresList = leaderboard.map { entry ->

            Log.d("Location - setupRecyclerView", "Converting entry with lat=${entry.latitude}, lng=${entry.longitude}")

            ScoreAdapter.ScoreEntry(
                pictureResId = R.drawable.user_avatar,
                name = entry.name,
                date = entry.date,
                score = entry.score,
                latitude = entry.latitude,
                longitude = entry.longitude
            )
        }

        val adapter = ScoreAdapter(scoresList) { scoreEntry ->
            Log.d("Location - Adapter", "Clicked entry with lat=${scoreEntry.latitude}, lng=${scoreEntry.longitude}")

            // Handle item click
            scoreEntry.latitude?.let { lat ->
                scoreEntry.longitude?.let { lng ->
                    // Find the MapFragment and move camera to location
                    val mapFragment = parentFragmentManager.findFragmentById(R.id.map_frame) as? MapFragment
                    mapFragment?.moveToLocation(lat, lng, "${scoreEntry.name}: ${scoreEntry.score}")
                }
            }
        }

        leaderboardRecyclerView.layoutManager = LinearLayoutManager(context)
        leaderboardRecyclerView.adapter = adapter
    }
}