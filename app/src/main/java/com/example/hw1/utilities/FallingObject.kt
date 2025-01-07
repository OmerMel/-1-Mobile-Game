package com.example.hw1.utilities

import com.example.hw1.enums.FallingObjectType

data class FallingObject(
    val type: FallingObjectType,
    var columnIndex: Int
)
