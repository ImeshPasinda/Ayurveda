package com.example.ayurveda

import android.animation.AnimatorInflater
import android.animation.ObjectAnimator
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatDelegate
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class Home : AppCompatActivity() {

    // Initialize Firestore
    val db = Firebase.firestore



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val sessionManager = SessionManager(this)
        val userEmail = sessionManager.getUserEmail()

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);



        // Retrieve the username from the "users" collection
        val usersCollection = db.collection("users")
        usersCollection.whereEqualTo("email", userEmail)
            .get()
            .addOnSuccessListener { userQuerySnapshot ->
                // Assuming there's only one document with the matching email
                if (!userQuerySnapshot.isEmpty) {
                    val userDocument = userQuerySnapshot.documents[0]
                    val username = userDocument.getString("username")

                    // Now you have the username, and you can use it as needed
                    // For example, you can set it in a TextView
                    val usernameTextView = findViewById<TextView>(R.id.unamenavbar)
                    usernameTextView.text = username
                }
            }
            .addOnFailureListener { exception ->
                // Handle the failure to retrieve user data
                Log.e("Firestore", "Error getting user document: $exception")
            }


        // Create a list to hold the data
        val doctorsList = mutableListOf<Doctor>()

        val doctorsCollection = db.collection("doctors")
        doctorsCollection.get()
            .addOnSuccessListener { querySnapshot ->
                for (document in querySnapshot.documents) {
                    val docNameEn = document.getString("docNameEn")
                    val docNameSn = document.getString("docNameSn")
                    val avatarUrl = document.getString("avatarUrl")
                    val docQualification = document.getString("docQualification")
                    val docLicense = document.getString("docLicense")
                    val docPhoneNo = document.getString("docPhoneNo")
                    val address = document.getString("address")
                    val longitude = document.getDouble("longitude")
                    val latitude = document.getDouble("latitude")

                    if (docNameEn != null && docNameSn != null && avatarUrl != null &&
                        docQualification != null && docLicense != null && docPhoneNo != null &&
                        address != null && longitude != null && latitude != null) {
                        val doctor = Doctor(
                            docNameEn,
                            docNameSn,
                            avatarUrl,
                            docQualification,
                            docLicense,
                            docPhoneNo,
                            address,
                            longitude,
                            latitude
                        )
                        doctorsList.add(doctor)
                    }
                }

                // Set up RecyclerView
                val recyclerView = findViewById<RecyclerView>(R.id.recycleviewdoctors)
                val adapter = DoctorAdapter(doctorsList)
                recyclerView.adapter = adapter
                recyclerView.layoutManager = LinearLayoutManager(this)
            }
            .addOnFailureListener { exception ->
                // Handle the failure to retrieve data
                Log.e("Firestore", "Error getting documents: $exception")
            }


    }
    // Override the onBackPressed() method to block the back button
    override fun onBackPressed() {
        // Leave this method empty to block the back button
    }
}
