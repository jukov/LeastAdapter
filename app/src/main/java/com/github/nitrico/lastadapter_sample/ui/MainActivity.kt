package com.github.nitrico.lastadapter_sample.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import info.jukov.leastadapter_sample.R

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))

        savedInstanceState ?: supportFragmentManager.beginTransaction()
            .replace(R.id.container, ListFragment())
            .commit()
    }
}
