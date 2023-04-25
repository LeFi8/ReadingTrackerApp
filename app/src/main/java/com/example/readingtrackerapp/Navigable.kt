package com.example.readingtrackerapp

interface Navigable {
    enum class Destination {
        List, Edit
    }

    fun navigate(to: Destination)

    fun navigateWithBookId(to: Destination, id: Long)
}