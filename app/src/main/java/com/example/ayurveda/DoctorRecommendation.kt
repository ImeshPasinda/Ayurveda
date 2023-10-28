package com.example.ayurveda

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.SearchView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore

class DoctorRecommendation : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var doctorArrayList: ArrayList<Doctor>
    private lateinit var doctorAdapter: DoctorAdapter
    private lateinit var db: FirebaseFirestore
    private lateinit var searchView: SearchView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_doctor_recommendation)

        //home
        val backButton = findViewById<ImageButton>(R.id.doctorbackbtn)
        backButton.setOnClickListener {
            val intent = Intent(this, Home::class.java)
            startActivity(intent)
        }

        //Profile
        db = FirebaseFirestore.getInstance()
        val sessionManager = SessionManager(this)
        val userEmail = sessionManager.getUserEmail()



        val userNavButton = findViewById<ImageButton>(R.id.userProfileNavbtn)
        userNavButton.setOnClickListener {
            val intent = Intent(this, UserProfile::class.java)
            startActivity(intent)
        }
        // Retrieve the username from the "users" collection
        val usersCollection = db.collection("users")
        usersCollection.whereEqualTo("email", userEmail)
            .get()
            .addOnSuccessListener { userQuerySnapshot ->
                if (!userQuerySnapshot.isEmpty) {
                    // There may be multiple documents matching the email; loop through them if needed
                    for (userDocument in userQuerySnapshot.documents) {
                        val username = userDocument.getString("username")

                        // Now you have the username, and you can use it as needed
                        // For example, you can set it in a TextView
                        val usernameTextView = findViewById<TextView>(R.id.unamenavbar)
                        usernameTextView.text = username?.split(" ")?.get(0) ?: "User"

                        // If you found the username you were looking for, you can break out of the loop
                        break
                    }
                } else {
                    // Handle the case when no document with the matching email is found
                    Log.d("Firestore", "No user document found for email: $userEmail")
                }
            }
            .addOnFailureListener { exception ->
                // Handle the failure to retrieve user data
                Log.e("Firestore", "Error getting user document: $exception")
            }

        //remedy
        val remediesBtn = findViewById<Button>(R.id.remediesbtn)
        remediesBtn.setOnClickListener {
            val intent = Intent(this, RemedyRecommendation::class.java)
            startActivity(intent)
        }

        //herbs
        val herbsBtn = findViewById<Button>(R.id.herbsbtn)
        herbsBtn.setOnClickListener {
            val intent = Intent(this, HerbRecommendation::class.java)
            startActivity(intent)
        }

        //appoinments
        val appBtn = findViewById<ImageButton>(R.id.userAppointmentsbtn)
        appBtn.setOnClickListener {
            val intent = Intent(this, UserAppointments::class.java)
            startActivity(intent)
        }

        //store
        val storeBtn = findViewById<ImageButton>(R.id.storenavbtn)
        storeBtn.setOnClickListener {
            val intent = Intent(this, StoreHome::class.java)
            startActivity(intent)
        }

        //home
        val homeBtn = findViewById<ImageButton>(R.id.homenavbtn_1)
        homeBtn.setOnClickListener {
            val intent = Intent(this, Home::class.java)
            startActivity(intent)
        }



        recyclerView = findViewById(R.id.recycleviewdoctors)
        searchView = findViewById(R.id.searchViewDoctor)

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)

        doctorArrayList = ArrayList()
        doctorAdapter = DoctorAdapter(doctorArrayList)
        recyclerView.adapter = doctorAdapter



        db.collection("doctors").get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    val docNameEn = document.getString("docNameEn")
                    val docNameSn = document.getString("docNameSn")
                    val category = document.getString("category")
                    val avatarUrl = document.getString("avatarUrl")
                    val docQualification = document.getString("docQualification")
                    val docLicense = document.getString("docLicense")
                    val docPhoneNo = document.getString("docPhoneNo")
                    val address = document.getString("address")
                    val longitude = document.getDouble("longitude")
                    val latitude = document.getDouble("latitude")

                    if (docNameEn != null && docNameSn != null && category != null &&
                        avatarUrl != null && docQualification != null && docLicense != null &&
                        docPhoneNo != null && address != null && longitude != null &&
                        latitude != null) {
                        val doctor = Doctor(
                            docNameEn,
                            docNameSn,
                            category,
                            avatarUrl,
                            docQualification,
                            docLicense,
                            docPhoneNo,
                            address,
                            longitude,
                            latitude
                        )
                        doctorArrayList.add(doctor)
                    }
                }
                doctorAdapter.updateData(doctorArrayList)
            }
            .addOnFailureListener { exception ->
                // Handle the error
            }

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filterDoctorList(newText)
                return true
            }
        })
    }

    private fun filterDoctorList(query: String?) {
        val filteredDoctors = if (query.isNullOrBlank()) {
            doctorArrayList
        } else {
            doctorArrayList.filter { doctor ->
                doctor.category.contains(query, ignoreCase = true)
            }
        }
        doctorAdapter.updateData(filteredDoctors)
    }
}