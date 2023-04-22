package com.example.readingtrackerapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val listFragment = ListFragment()
        val editFragment = EditFragment()
        supportFragmentManager.beginTransaction()
//            .add(R.id.container, editFragment, listFragment.javaClass.name)
            .add(R.id.container, listFragment, listFragment.javaClass.name)
            .commit()
    }
}