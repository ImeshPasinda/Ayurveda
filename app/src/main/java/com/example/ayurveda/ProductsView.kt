package com.example.ayurveda

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.google.firebase.firestore.FirebaseFirestore

class ProductsView : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_products_view)

        val db = FirebaseFirestore.getInstance()

        // Retrieve the product data from the intent's extras
        val productNameEn = intent.getStringExtra("productNameEn")
        val productNameSn = intent.getStringExtra("productNameSn")
        val productDescription = intent.getStringExtra("productDescription")
        val price = intent.getDoubleExtra("price", 0.0)
        val productImg = intent.getStringExtra("productImg")

        // Assign the values to related IDs in your layout
        val productNameEnTextView = findViewById<TextView>(R.id.prodNameEnpage)
        val productNameSnTextView = findViewById<TextView>(R.id.prodNameSnpage)
        val productsubNameEnTextView = findViewById<TextView>(R.id.prodSubNameEnpage)
        val productsubNameSnTextView = findViewById<TextView>(R.id.prodSubNameSnpage)
        val productDescriptionTextView = findViewById<TextView>(R.id.proddescpage)
        val priceTextView = findViewById<TextView>(R.id.prodpricepage)
        val productImageView = findViewById<ImageView>(R.id.prodImgpage)

        productNameEnTextView.text = productNameEn
        productNameSnTextView.text = productNameSn
        productsubNameEnTextView.text = productNameEn
        productsubNameSnTextView.text = productNameSn
        productDescriptionTextView.text = productDescription
        priceTextView.text = price.toString()

        // Load and display the product image using Glide
        Glide.with(this)
            .load(productImg)
            .into(productImageView)


        val btnAddToCart = findViewById<Button>(R.id.addtocart)

        btnAddToCart.setOnClickListener {
            val intent = Intent(this, StoreCart::class.java)
            intent.putExtra("productNameEn", productNameEn)
            intent.putExtra("productNameSn", productNameSn)
            intent.putExtra("price", price)
            intent.putExtra("productImg", productImg)
            startActivity(intent)
        }


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
    }


}