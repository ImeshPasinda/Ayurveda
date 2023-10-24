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

class DoctorLogin : AppCompatActivity() {
    private val db = FirebaseFirestore.getInstance()
    private lateinit var sessionManager: SessionManager

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_doctor_login)

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        sessionManager = SessionManager(this)



        val btnDoctorLogin = findViewById<Button>(R.id.userlogoutbtn)
        val btnDoctorSignUp = findViewById<ImageButton>(R.id.docsignupbtn)

        btnDoctorSignUp.setOnClickListener {
            val intent = Intent(this, DoctorRegister::class.java)
            startActivity(intent)
        }
        btnDoctorLogin.setOnClickListener {
            val email = findViewById<EditText>(R.id.ulemail).text.toString()
            val password = findViewById<EditText>(R.id.ulpassword).text.toString()

            // Query Firestore to check if the email and password match for doctors
            db.collection("doctors")
                .whereEqualTo("email", email)
                .whereEqualTo("password", password)
                .get()
                .addOnSuccessListener { documents ->
                    if (!documents.isEmpty) {
                        // User found, navigate to DoctorProfile activity
                        val intent = Intent(this, DoctorProfileOwn::class.java)
                        startActivity(intent)
                        // Save the user session
                        sessionManager.saveUserSession(email)
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


    }
}
