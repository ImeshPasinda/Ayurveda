package com.example.ayurveda

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
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

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        // Create a list to hold the data
        val doctorsList = mutableListOf<Doctor>()

        // Retrieve data from Firestore
        val doctorsCollection = db.collection("doctors")
        doctorsCollection.get()
            .addOnSuccessListener { querySnapshot ->
                for (document in querySnapshot.documents) {
                    val docNameEn = document.getString("docNameEn")
                    val docNameSn = document.getString("docNameSn")
                    val avatarUrl = document.getString("avatarUrl")

                    if (docNameEn != null && docNameSn != null && avatarUrl != null) {
                        val doctor = Doctor(docNameEn, docNameSn, avatarUrl)
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
}
