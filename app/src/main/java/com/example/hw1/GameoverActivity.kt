package com.example.hw1

import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.hw1.fragments.LeaderboardFragment

class GameoverActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gameover)

        init()
        loadLeaderboardFragment()
    }

    private fun init() {
        val bundle = intent.extras
        val score = bundle?.getInt("score")
        val msg = bundle?.getString("msg")
    }



    private fun loadLeaderboardFragment() {
        val fragmentManager: FragmentManager = supportFragmentManager
        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()

        val leaderboardFragment = LeaderboardFragment()
        fragmentTransaction.replace(R.id.leaderboard_frame, leaderboardFragment)
        fragmentTransaction.commit()
    }

}