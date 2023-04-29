package com.example.readingtrackerapp.model

import androidx.annotation.DrawableRes

data class Book (
    val id: Long,
    val title: String,
    val status: String,
    var currentPage: Int,
    val maxPages: Int,
    @DrawableRes
    val resId: Int
)