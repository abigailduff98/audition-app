package com.example.audition

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Utility.hideSystemUI(window)
    }

    override fun onStart() {
        super.onStart()
        Utility.hideSystemUI(window)
    }

}
