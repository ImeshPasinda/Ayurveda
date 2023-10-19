package com.example.ayurveda

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatDelegate
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class UserAppointments : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_appointments)

        val pendingRecyclerView = findViewById<RecyclerView>(R.id.pendingbookingrecyclerView)
        val completedRecyclerView = findViewById<RecyclerView>(R.id.completedbookingrecyclerView)

        pendingRecyclerView.layoutManager = LinearLayoutManager(this)
        completedRecyclerView.layoutManager = LinearLayoutManager(this)

        val pendingAppointments = mutableListOf<Appointment>()
        val completedAppointments = mutableListOf<Appointment>()

        val pendingAdapter = PendingAppointmentAdapter(pendingAppointments)
        val completedAdapter = CompletedAppointmentAdapter(completedAppointments)

        pendingRecyclerView.adapter = pendingAdapter
        completedRecyclerView.adapter = completedAdapter










        val db = FirebaseFirestore.getInstance()





        // Navbar
        val sessionManager = SessionManager(this)
        val userEmail = sessionManager.getUserEmail()

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        //Profile
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






        val appointmentsCollection = db.collection("appointments")

        // Create a query to filter appointments by email and status
        val pendingquery: Query = appointmentsCollection
            .whereEqualTo("patientEmail", userEmail)
            .whereEqualTo("status", "pending")

        pendingquery.get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    val doctorName = document.getString("doctorName") ?: ""
                    val bookingTime = document.getString("bookingTime") ?: ""
                    val bookingDate = document.getString("bookingDate") ?: ""

                    val appointment = Appointment(doctorName, bookingTime, bookingDate)
                    pendingAppointments.add(appointment)
                }
                pendingAdapter.notifyDataSetChanged()
            }
            .addOnFailureListener { exception ->
                // Handle errors here
                Log.e("FirestoreError", "Error fetching data from Firestore: $exception")
            }

        val completedquery: Query = appointmentsCollection
            .whereEqualTo("patientEmail", userEmail)
            .whereEqualTo("status", "completed")

        completedquery.get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    val doctorName = document.getString("doctorName") ?: ""
                    val bookingTime = document.getString("bookingTime") ?: ""
                    val bookingDate = document.getString("bookingDate") ?: ""

                    val appointment = Appointment(doctorName, bookingTime, bookingDate)
                    completedAppointments.add(appointment)
                }
                completedAdapter.notifyDataSetChanged()
            }
            .addOnFailureListener { exception ->
                // Handle errors here
                Log.e("FirestoreError", "Error fetching data from Firestore: $exception")
            }






    }
}