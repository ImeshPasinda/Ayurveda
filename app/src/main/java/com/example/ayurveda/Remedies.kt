package com.example.ayurveda

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.TextView
import com.google.firebase.firestore.FirebaseFirestore

class Remedies : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_remedies)

        val db = FirebaseFirestore.getInstance()

        // val category = intent.getStringExtra("category")
        val description = intent.getStringExtra("description")
        val doctorName = intent.getStringExtra("doctorName")
        val title = intent.getStringExtra("title")

        val subTitleTextView = findViewById<TextView>(R.id.remedySubTitle)
        val descriptionTextView = findViewById<TextView>(R.id.remedydescpage)
        val doctorNameTextView = findViewById<TextView>(R.id.docNamePtxt9)
        val titleTextView = findViewById<TextView>(R.id.remedyTitle)

        subTitleTextView.text = title
        descriptionTextView.text = description
        doctorNameTextView.text = doctorName
        titleTextView.text = title

        val sessionManager = SessionManager(this)
        val userEmail = sessionManager.getUserEmail()

        val userNavButton = findViewById<ImageButton>(R.id.userProfileNavbtn)
        userNavButton.setOnClickListener {
            val intent = Intent(this, UserProfile::class.java)
            startActivity(intent)
        }

        val usersCollection = db.collection("users")
        usersCollection.whereEqualTo("email", userEmail)
            .get()
            .addOnSuccessListener { userQuerySnapshot ->
                if (!userQuerySnapshot.isEmpty) {
                    for (userDocument in userQuerySnapshot.documents) {
                        val username = userDocument.getString("username")
                        val usernameTextView = findViewById<TextView>(R.id.unamenavbar)
                        usernameTextView.text = username?.split(" ")?.get(0) ?: "User"
                        break
                    }
                } else {
                    Log.d("Firestore", "No user document found for email: $userEmail")
                }
            }
            .addOnFailureListener { exception ->
                Log.e("Firestore", "Error getting user document: $exception")
            }
    }
}