package com.example.ayurveda

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class DoctorPayment : AppCompatActivity() {

    // Initialize Firestore
    val db = Firebase.firestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_doctor_payment)


        // Retrieve the bookingDate and bookingTime from the intent
        val bookingDate = intent.getStringExtra("bookingDate")
        val bookingTime = intent.getStringExtra("bookingTime")
        val doctorName = intent.getStringExtra("doctorName")

        // Find the TextView or any other view where you want to display the data
        val dateTextView = findViewById<TextView>(R.id.docPayDate)
        val timeTextView = findViewById<TextView>(R.id.docPayTime)

        // Display the data in the TextViews
        dateTextView.text = bookingDate
        timeTextView.text = bookingTime















        // Navbar
        val sessionManager = SessionManager(this)
        val userEmail = sessionManager.getUserEmail()

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

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

                        // Find the EditText views
                        val editTextUsername = findViewById<EditText>(R.id.pNamepayment)
                        val editTextEmail = findViewById<EditText>(R.id.pEmailPayment)


                        editTextUsername.setText(username)
                        editTextEmail.setText(userEmail)

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


        val doctorPayButton = findViewById<Button>(R.id.doctorpaybtn)
        doctorPayButton.setOnClickListener {
            // Create an intent to start the PaymentGateway activity
            val intent = Intent(this, PaymentGateway::class.java)

            // Get the values from the EditText views
            val ppNumberPayment = findViewById<EditText>(R.id.ppNumberPayment)
            val pAddressPayment = findViewById<EditText>(R.id.pAddressPayment)

            // Check if the fields are empty
            val ppNumberText = ppNumberPayment.text.toString()
            val pAddressText = pAddressPayment.text.toString()

            // Check if the fields are empty
            if (ppNumberText.isEmpty() || pAddressText.isEmpty()) {
                // Display an error message or toast to inform the user that fields are required
                // You can use a Toast or any other UI element to notify the user
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            } else {
                // Fields are not empty, proceed to send data to PaymentGateway activity


                // Add the data as extras to the intent
                intent.putExtra("bookingDate", bookingDate)
                intent.putExtra("bookingTime", bookingTime)
                intent.putExtra("bookingPhoneNo", ppNumberText)
                intent.putExtra("bookingAddress", pAddressText)
                intent.putExtra("doctorName", doctorName)

                // Start the PaymentGateway activity with the intent
                startActivity(intent)
            }
        }




    }
}