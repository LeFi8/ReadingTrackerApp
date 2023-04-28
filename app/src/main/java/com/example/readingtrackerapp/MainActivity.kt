package com.example.readingtrackerapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.readingtrackerapp.fragments.EditFragment
import com.example.readingtrackerapp.fragments.ListFragment

class MainActivity : AppCompatActivity(), Navigable {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()

        val listFragment = ListFragment()
        supportFragmentManager.beginTransaction()
            .add(R.id.container, listFragment, listFragment.javaClass.name)
            .commit()
    }

    override fun navigate(to: Navigable.Destination, id: Long) {
        supportFragmentManager.beginTransaction().apply {
            when (to) {
                Navigable.Destination.List -> replace(
                    R.id.container,
                    ListFragment(),
                    ListFragment::class.java.name
                )
                Navigable.Destination.Add -> {
                    replace(R.id.container, EditFragment(), EditFragment::class.java.name)
                    addToBackStack(EditFragment::class.java.name)
                }
                Navigable.Destination.Edit -> {
                    replace(R.id.container, EditFragment(id), EditFragment::class.java.name)
                    addToBackStack(EditFragment::class.java.name)
                }
            }.commit()
        }
    }
}


