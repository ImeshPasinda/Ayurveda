package com.example.ayurveda

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        val sessionManager = SessionManager(this)

        if (sessionManager.isUserLoggedIn()) {
            // User is already logged in, go to Home activity
            val intent = Intent(this, Home::class.java)
            startActivity(intent)
            finish() // Close the main activity
        } else {
            // User is not logged in, go to the login screen
            val intent = Intent(this, UsersLogin::class.java)
            startActivity(intent)
            finish()
        }
    }
}
