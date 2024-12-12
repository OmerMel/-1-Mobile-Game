package com.example.hw1.utilities

import android.util.Log
import androidx.appcompat.widget.AppCompatImageView

class GameManage(private val lifeCount: Int = 3) {
    var score: Int = 0
        private set

    var currentCatcherIndex: Int = 0
        private set

    var injuries: Int = 0
        private set

    val isGameOver: Boolean
        get() = injuries == lifeCount

    fun addInjurie() {
        injuries++
    }
}