package com.example.readingtrackerapp

interface Navigable {
    enum class Destination {
        List, Add, Edit
    }

    fun navigate(to: Destination)

    fun navigateWithBookId(to: Destination, id: Long)
}