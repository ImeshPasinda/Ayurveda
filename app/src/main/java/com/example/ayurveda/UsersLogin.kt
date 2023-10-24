package com.example.ayurveda

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import com.google.firebase.firestore.FirebaseFirestore

class UsersLogin : AppCompatActivity() {
    private val db = FirebaseFirestore.getInstance()

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_users_login)

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        val btnUsersLogin = findViewById<Button>(R.id.userlogoutbtn)

        btnUsersLogin.setOnClickListener {
            val email = findViewById<EditText>(R.id.ulemail).text.toString()
            val password = findViewById<EditText>(R.id.ulpassword).text.toString()

            // Query Firestore to check if the email and password match
            db.collection("users")
                .whereEqualTo("email", email)
                .whereEqualTo("password", password)
                .get()
                .addOnSuccessListener { documents ->
                    if (!documents.isEmpty) {
                        val sessionManager = SessionManager(this)
                        sessionManager.saveUserSession(email)
                        // User found, navigate to Home activity
                        val intent = Intent(this, Home::class.java)
                        startActivity(intent)
                    } else {
                        // User not found or password does not match, handle the error (e.g., show an error message)
                        Toast.makeText(this, "Authentication failed.", Toast.LENGTH_SHORT).show()
                    }
                }
                .addOnFailureListener { exception ->
                    // Handle exceptions here (e.g., Firestore query error)
                    Toast.makeText(this, "Error: ${exception.message}", Toast.LENGTH_SHORT).show()
                }
        }

        val btnSignUp = findViewById<ImageButton>(R.id.signupbtn)

        btnSignUp.setOnClickListener {
            val intent = Intent(this, UsersRegister::class.java)
            startActivity(intent)
        }

        val SwicthDoctorPortal = findViewById<ImageButton>(R.id.switchtoDoctorbtn)

        SwicthDoctorPortal.setOnClickListener {
            val intent = Intent(this, DoctorLogin::class.java)
            startActivity(intent)
        }
    }
}