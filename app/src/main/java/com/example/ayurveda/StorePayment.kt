package com.example.ayurveda

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import com.bumptech.glide.Glide
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class StorePayment : AppCompatActivity() {

    // Initialize Firestore
    val db = Firebase.firestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_store_payment)

        // Retrieve the extras sent from StoreCart
        val productNameEn = intent.getStringExtra("productNameEn")
        val productNameSn = intent.getStringExtra("productNameSn")
        val productImg = intent.getStringExtra("productImg")
        val totalPrice = intent.getDoubleExtra("totalPrice", 0.0)

        // Find the TextView elements in your layout
        val ppImge = findViewById<ImageView>(R.id.ppimg)
        val ppnameen = findViewById<TextView>(R.id.ppnameen)
        val ppnamesn = findViewById<TextView>(R.id.ppnamesn)
        val ppprice = findViewById<TextView>(R.id.ppprice)

        // Load the product image using Glide
        Glide.with(this)
            .load(productImg)
            .into(ppImge)

        // Set the text for product name, product name in another language, and total price
        ppnameen.text = productNameEn
        ppnamesn.text = productNameSn
        ppprice.text = String.format("%.2f", totalPrice)



        //Navbar

        val sessionManager = SessionManager(this)
        val userEmail = sessionManager.getUserEmail()

        //Profile
        val userNavButton = findViewById<ImageButton>(R.id.userProfileNavbtn)
        userNavButton.setOnClickListener {
            val intent = Intent(this, UserProfile::class.java)
            startActivity(intent)
        }
        //Home
        val homeBtn = findViewById<ImageButton>(R.id.homenavbtn_1)
        homeBtn.setOnClickListener {
            val intent = Intent(this, Home::class.java)
            startActivity(intent)
        }
        //Appointments
        val userAppointmentButton = findViewById<ImageButton>(R.id.userAppointmentsbtn)
        userAppointmentButton.setOnClickListener {
            val intent = Intent(this, UserAppointments::class.java)
            startActivity(intent)
        }
        //Store
        val userStoreButton = findViewById<ImageButton>(R.id.storenavbtn)
        userStoreButton.setOnClickListener {
            val intent = Intent(this, StoreHome::class.java)
            startActivity(intent)
        }
        //Remedy
        val RemedyButton = findViewById<ImageButton>(R.id.remedyNavBtn)
        RemedyButton.setOnClickListener {
            val intent = Intent(this, DoctorRecommendation::class.java)
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
                        val editTextUsername = findViewById<EditText>(R.id.ppnameinput)
                        val editTextEmail = findViewById<EditText>(R.id.ppemailinput)


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



        // Find the EditText elements in your layout
        val pppnumberinput = findViewById<EditText>(R.id.pppnumberinput)
        val ppaddressinput = findViewById<EditText>(R.id.ppaddressinput)
        val pppaynowbtn = findViewById<Button>(R.id.pppaynowbtn)

        pppaynowbtn.setOnClickListener {
            // Get the text from the EditText fields
            val phoneNumber = pppnumberinput.text.toString()
            val address = ppaddressinput.text.toString()

            if (phoneNumber.isBlank() || address.isBlank()) {
                // Display a single Toast message if either of the fields is empty
                Toast.makeText(
                    this,
                    "Phone number and address cannot be empty",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                // All fields are filled, create an Intent and start PaymentGatewayActivity
                val intent = Intent(this, StorePaymentGateway::class.java)
                intent.putExtra("phoneNumber", phoneNumber)
                intent.putExtra("address", address)
                intent.putExtra("price", totalPrice)
                intent.putExtra("productName", productNameEn)
                startActivity(intent)
            }
        }


    }
}