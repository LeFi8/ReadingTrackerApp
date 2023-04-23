package com.example.readingtrackerapp.model

import androidx.annotation.DrawableRes

data class Book (
    val title: String,
    val status: String,
    val currentPage: Int,
    val maxPages: Int,
    @DrawableRes
    val resId: Int
)