package com.example.ayurveda

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class PaymentGateway : AppCompatActivity() {

    // Initialize Firestore
    val db = Firebase.firestore
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment_gateway)


    // Retrieve the data from the intent
    val bookingDate = intent.getStringExtra("bookingDate")
    val bookingTime = intent.getStringExtra("bookingTime")
    val ppNumberPayment = intent.getStringExtra("bookingPhoneNo")
    val pAddressPayment = intent.getStringExtra("bookingAddress")
    val doctorName = intent.getStringExtra("doctorName")






    // Navbar
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
        // Get references to EditText views
        val cvvEditText = findViewById<EditText>(R.id.cvv)
        val cardNumberEditText = findViewById<EditText>(R.id.cardNo)
        val expiryDateEditText = findViewById<EditText>(R.id.expireDate)

        val paybtn = findViewById<Button>(R.id.paymentGatewaybtn)

        paybtn.setOnClickListener {
            val cvv = cvvEditText.text.toString()
            val cardNumber = cardNumberEditText.text.toString()
            val expiryDate = expiryDateEditText.text.toString()

            if (cvv.isEmpty() || cardNumber.isEmpty() || expiryDate.isEmpty()) {
                // Show an error message
                Toast.makeText(this, "Please input all fields", Toast.LENGTH_SHORT).show()
            } else {
                // All fields are filled, proceed with payment
                val usersCollection = db.collection("users")
                usersCollection.whereEqualTo("email", userEmail)
                    .get()
                    .addOnSuccessListener { userQuerySnapshot ->
                        if (!userQuerySnapshot.isEmpty) {
                            for (userDocument in userQuerySnapshot.documents) {
                                val username = userDocument.getString("username")

                                val appointmentsCollection = db.collection("appointments")
                                val appointmentData = hashMapOf(
                                    "bookingDate" to bookingDate,
                                    "patientName" to username,
                                    "patientEmail" to userEmail,
                                    "doctorName" to doctorName,
                                    "bookingTime" to bookingTime,
                                    "bookingAddress" to pAddressPayment,
                                    "bookingPhoneNo" to ppNumberPayment,
                                    "status" to "pending",
                                    "symptoms" to null,
                                    "medications" to null,
                                    "notes" to null,
                                )

                                appointmentsCollection.add(appointmentData)
                                    .addOnSuccessListener { documentReference ->
                                        // Data added successfully, start PaymentSuccess activity with bookingDate
                                        val intent = Intent(this, PaymentSuccess::class.java)
                                        intent.putExtra("refno", ppNumberPayment)
                                        startActivity(intent)
                                    }
                                    .addOnFailureListener { e ->

                                    }
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




    }

}