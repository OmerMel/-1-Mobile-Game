package com.example.hw1.logic

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

    fun addScore(amount: Int){
        score = 0.coerceAtLeast(score + amount)
    }
}