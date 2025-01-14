package com.example.hw1.enums

import com.example.hw1.R

enum class FallingObjectType (
    val imageResource: Int,
    val points: Int,
    val probability: Double,
    val effect: GameEffect,
    val sound: Int
) {
    CUCUMBER(
        imageResource = R.drawable.cucumber,
        points = -1,
        probability = 0.4,
        effect = GameEffect.DAMAGE,
        sound = R.raw.spew
    ),
    HAMBURGER(
        imageResource = R.drawable.burger,
        points = 10,
        probability = 0.9,
        effect = GameEffect.NONE,
        sound = R.raw.eating_sound
    );

    companion object {
        fun getRandomObject(): FallingObjectType {
            val totalProbability = entries.sumOf { it.probability }
            val random = Math.random() * totalProbability // Adjust random range
            var cumulativeProbability = 0.0

            for (type in entries) {
                cumulativeProbability += type.probability
                if (random <= cumulativeProbability) {
                    return type
                }
            }
            return HAMBURGER
        }
    }

}