package com.example.readingtrackerapp

import androidx.annotation.DrawableRes

data class Book (
    val title: String,
    val status: String,
    val currentPage: Int,
    @DrawableRes
    val resId: Int
)