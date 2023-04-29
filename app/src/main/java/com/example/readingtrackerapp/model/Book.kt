package com.example.readingtrackerapp.model

import androidx.annotation.DrawableRes

data class Book (
    val id: Long,
    val title: String,
    var status: String,
    var currentPage: Int,
    var maxPages: Int,
    @DrawableRes
    val resId: Int
)