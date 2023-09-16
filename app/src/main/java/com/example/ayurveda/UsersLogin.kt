package com.example.ayurveda

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatDelegate

class UsersLogin : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_users_login)

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        val btnUsersLogin= findViewById<Button>(R.id.userloginbtn)

        btnUsersLogin.setOnClickListener {
            val intent = Intent(this, Home::class.java)
            startActivity(intent)
        }

        val btnSignUp= findViewById<ImageButton>(R.id.signupbtn)

        btnSignUp.setOnClickListener {
            val intent = Intent(this, UsersRegister::class.java)
            startActivity(intent)
        }
    }
}