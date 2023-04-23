package com.example.readingtrackerapp.data

import com.example.readingtrackerapp.model.Book
import com.example.readingtrackerapp.R

object DataSource {

    val books = mutableListOf<Book>(
        Book (
            "The Martian", "Completed", 369, 369, R.drawable.martian
        ),
        Book (
            "Atomic Habits", "Reading", 169, 251, R.drawable.atomic_habits
        ),
        Book (
            "1984", "Not Started", 0, 328, R.drawable.book
        ),
        Book (
            "The Witcher: Last Wish",  "Completed", 288, 288, R.drawable.book
        ),
        Book (
            "Solo Leveling", "Reading", 43, 179, R.drawable.manga
        )
    )
}