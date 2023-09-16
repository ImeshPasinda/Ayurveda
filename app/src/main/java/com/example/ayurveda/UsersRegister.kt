package com.example.ayurveda

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton

class UsersRegister : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_users_register)


        val btnUsersRegister= findViewById<Button>(R.id.userregisterbtn)

        btnUsersRegister.setOnClickListener {
            val intent = Intent(this, WelcomeUser::class.java)
            startActivity(intent)
        }

        val btnSignIn= findViewById<ImageButton>(R.id.signinbtn)

        btnSignIn.setOnClickListener {
            val intent = Intent(this, UsersLogin::class.java)
            startActivity(intent)
        }
    }
}