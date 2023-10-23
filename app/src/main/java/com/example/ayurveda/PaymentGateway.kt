package com.example.ayurveda

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.NotificationCompat
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

            // Define regular expressions for the expected formats
            val cvvPattern = Regex("^\\d{3}$")
            val cardNumberPattern = Regex("^\\d{16}$")
            val expiryDatePattern = Regex("^(0[1-9]|1[0-2])/\\d{2}$")

            if (!cardNumberPattern.matches(cardNumber)) {
                // Show an error message for invalid card number
                Toast.makeText(this, "Invalid card number", Toast.LENGTH_SHORT).show()
            } else if (!expiryDatePattern.matches(expiryDate)) {
                // Show an error message for invalid expiry date
                Toast.makeText(this, "Invalid expiry date (use MM/YY format)", Toast.LENGTH_SHORT).show()
            } else if (!cvvPattern.matches(cvv)) {
                // Show an error message for invalid CVV
                Toast.makeText(this, "CVV must be 3 numbers", Toast.LENGTH_SHORT).show()
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

                                        val productId = documentReference.id
                                        // Data added successfully, start PaymentSuccess activity with bookingDate

                                        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

                                        val notificationId = 1
                                        val channelId = "my_channel_id"
                                        val channelName = "My Channel"
                                        val importance = NotificationManager.IMPORTANCE_HIGH
                                        val notificationTitle = "Payement send successfully!"
                                        val notificationText = "A Transaction for LKR 1750.00 has been debited to $doctorName"

                                        // Create a notification channel (required for Android Oreo and above)
                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                            val channel = NotificationChannel(channelId, channelName, importance)
                                            notificationManager.createNotificationChannel(channel)
                                        }

                                        // Create a notification
                                        val notificationBuilder = NotificationCompat.Builder(this, channelId)
                                            .setSmallIcon(R.drawable.ayurveda_blue_logo)
                                            .setContentTitle(notificationTitle)
                                            .setContentText(notificationText)
                                            .setAutoCancel(true)

                                        // Show the notification
                                        notificationManager.notify(notificationId, notificationBuilder.build())
                                        val intent = Intent(this, PaymentSuccess::class.java)
                                        intent.putExtra("refno", productId)
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