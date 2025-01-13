package com.example.hw1.utilities

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlin.concurrent.Volatile

class SharedPreferencesManager private constructor(context: Context) {
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences(
        Constants.Leaderboard.LEADERBOARD_KEY,
        Context.MODE_PRIVATE
    )

    companion object {
        @Volatile
        private var instance: SharedPreferencesManager? = null

        fun init(context: Context): SharedPreferencesManager {
            return instance ?: synchronized(this) {
                instance ?: SharedPreferencesManager(context).also { instance = it }
            }
        }

        fun getInstance(): SharedPreferencesManager {
            return instance ?: throw IllegalStateException("SharedPreferencesManager must be initialized by calling init(context) before use.")
        }
    }

    private fun putString(key: String, value: String) {
        with(sharedPreferences.edit()) {
            putString(key, value)
            apply()
        }
    }

    private fun getString(key: String, defaultValue: String): String {
        return sharedPreferences.getString(key, defaultValue) ?: defaultValue
    }

    fun putInt(key: String, value: Int) {
        with(sharedPreferences.edit()) {
            putInt(key, value)
            apply()
        }
    }

    fun getInt(key: String, defaultValue: Int): Int {
        return sharedPreferences.getInt(key, defaultValue)
    }

    fun putLeaderboard(key: String, leaderboard: List<LeaderboardEntry>) {
        val json = Gson().toJson(leaderboard)
        putString(key, json)
    }

    fun getLeaderboard(key: String): List<LeaderboardEntry> {
        val json = getString(key, "")
        return if (json.isNotEmpty()) {
            val type = object : TypeToken<List<LeaderboardEntry>>() {}.type
            Gson().fromJson(json, type)
        } else {
            emptyList()
        }
    }
}