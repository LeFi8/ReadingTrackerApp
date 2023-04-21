package com.example.readingtrackerapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val listFragment = ListFragment()
        supportFragmentManager.beginTransaction()
            .add(com.google.android.material.R.id.container, listFragment, listFragment.javaClass.name)
            .commit()
    }
}