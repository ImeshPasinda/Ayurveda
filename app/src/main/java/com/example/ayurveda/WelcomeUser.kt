package com.example.ayurveda

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class WelcomeUser : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome_user)

        // Customize the status bar color
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val window = window
            window.statusBarColor = getColor(R.color.white)
        }
    }
}