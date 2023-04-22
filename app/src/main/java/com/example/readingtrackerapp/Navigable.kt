package com.example.readingtrackerapp

interface Navigable {
    enum class Destination {
        List, Add
    }

    fun navigate(to: Destination)

}