package com.example.hw1.utilities

data class LeaderboardEntry(
    val name: String,
    val score: Int,
    val date: String,
    val latitude: Double? = null,
    val longitude: Double? = null

)
