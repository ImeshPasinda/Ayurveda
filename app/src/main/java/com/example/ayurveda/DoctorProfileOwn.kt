package com.example.ayurveda

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class DoctorProfileOwn : AppCompatActivity() {
    // Initialize Firestore
    val db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_doctor_profile_own)

        val sessionManager = SessionManager(this)
        val userEmail = sessionManager.getUserEmail()

        val doctorAppoinButton = findViewById<ImageButton>(R.id.userAppointmentsbtn)
        doctorAppoinButton.setOnClickListener {
            val intent = Intent(this, ViewAppoinments::class.java)
            startActivity(intent)
        }

        val doctorRemButton = findViewById<ImageButton>(R.id.remedyNavBtn)
        doctorRemButton.setOnClickListener {
            val intent = Intent(this,AddDoctorRemedy::class.java)
            startActivity(intent)
        }

        // Retrieve the username from the "users" collection
        val usersCollection = db.collection("doctors")
        usersCollection.whereEqualTo("email", userEmail)
            .get()
            .addOnSuccessListener { userQuerySnapshot ->
                if (!userQuerySnapshot.isEmpty) {
                    // There may be multiple documents matching the email; loop through them if needed
                    for (userDocument in userQuerySnapshot.documents) {
                        val username = userDocument.getString("docNameEn")

                        // Now you have the username, and you can use it as needed
                        // For example, you can set it in a TextView
                        val usernameTextView = findViewById<TextView>(R.id.unamenavbar)
                        usernameTextView.text = "Dr. " + (username?.split(" ")?.get(0) ?: "Doctor")

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

        // Retrieve the doctor's details from the "doctors" collection
        val doctorsCollection = db.collection("doctors")
        doctorsCollection.whereEqualTo("email", userEmail)

            .get()
            .addOnSuccessListener { doctorQuerySnapshot ->
                // Assuming there's only one document with the matching email
                if (!doctorQuerySnapshot.isEmpty) {
                    val doctorDocument = doctorQuerySnapshot.documents[0]
                    val doctorName = doctorDocument.getString("docNameEn")
                    val doctorAddress = doctorDocument.getString("address")
                    val doctorPhoneNo = doctorDocument.getString("docPhoneNo")
                    val doctorQualifications = doctorDocument.getString("docQualification")
                    val doctorRegNo = doctorDocument.getString("docLicense")


                    // Now you have the doctor's details, and you can use them as needed
                    // For example, you can set them in TextView elements

                    val docNamePtxt = findViewById<TextView>(R.id.docNameProf)
                    docNamePtxt.text = doctorName

                    val docAddressTextView = findViewById<TextView>(R.id.docAddr)
                    docAddressTextView.text = doctorAddress

                    val docPhoneNoTextView = findViewById<TextView>(R.id.docPhone)
                    docPhoneNoTextView.text = doctorPhoneNo

                    val docQualificationsTextView = findViewById<TextView>(R.id.docQual)
                    docQualificationsTextView.text = doctorQualifications

                    val docRegNoTextView = findViewById<TextView>(R.id.docRegNo)
                    docRegNoTextView.text = doctorRegNo

                }
            }
            .addOnFailureListener { exception ->
                // Handle the failure to retrieve doctor's data
                Log.e("Firestore", "Error getting doctor document: $exception")
            }

        val logoutButton = findViewById<Button>(R.id.doclogoutbtn)
        logoutButton.setOnClickListener {
            // Logout action
            sessionManager.logout()
            redirectToLoginActivity()
        }
    }

    private fun redirectToLoginActivity() {
        // Create an Intent to navigate to the LoginActivity
        val intent = Intent(this, DoctorLogin::class.java)
        // Clear the back stack so that the user cannot navigate back to DoctorProfileOwn
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }
    // Override the onBackPressed() method to block the back button
    override fun onBackPressed() {
        // Leave this method empty to block the back button
    }
}
