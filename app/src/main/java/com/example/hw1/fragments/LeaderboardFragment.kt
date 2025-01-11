package com.example.hw1.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.hw1.R
import com.example.hw1.ScoreAdapter
import com.example.hw1.utilities.Constants
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


class LeaderboardFragment : Fragment() {

    private lateinit var leaderboardRecyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_leaderboard, container, false)
        leaderboardRecyclerView = view.findViewById(R.id.leaderboard_RV_list)
        setupRecyclerView()
        return view
    }

    private fun setupRecyclerView() {
        val scores = getStoredScores()
        leaderboardRecyclerView.adapter = ScoreAdapter(scores.sortedByDescending { it.score })
        leaderboardRecyclerView.layoutManager = LinearLayoutManager(context).apply {
            orientation = RecyclerView.VERTICAL
        }
    }

    private fun getStoredScores(): List<ScoreAdapter.ScoreEntry> {
        val sharedPreferences = requireContext().getSharedPreferences(Constants.Leaderboard.PREFS_NAME, Context.MODE_PRIVATE)
        val gson = Gson()
        val json = sharedPreferences.getString(Constants.Leaderboard.SCORES_KEY, null)
        val type = object : TypeToken<List<ScoreAdapter.ScoreEntry>>() {}.type

        return if (json != null) {
            gson.fromJson(json, type)
        } else {
            emptyList()
        }
    }

    fun saveScore(name: String, score: Int) {
        val currentScores = getStoredScores().toMutableList()
        val newScore = ScoreAdapter.ScoreEntry(
            R.drawable.user_avatar,
            name,
            java.time.LocalDate.now().toString(),
            score
        )

        currentScores.add(newScore)

        val sharedPreferences = requireContext().getSharedPreferences(Constants.Leaderboard.PREFS_NAME, Context.MODE_PRIVATE)
        val gson = Gson()
        val json = gson.toJson(currentScores)

        sharedPreferences.edit().apply {
            putString(Constants.Leaderboard.SCORES_KEY, json)
            apply()
        }

        // Update RecyclerView
        setupRecyclerView()
    }

}