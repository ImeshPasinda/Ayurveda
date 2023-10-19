package com.example.ayurveda

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.bumptech.glide.Glide
import com.google.firebase.firestore.FirebaseFirestore

class StoreCart : AppCompatActivity() {
    private var quantity = 1 // Initialize quantity to 1
    private var price = 0.0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_store_cart)

        val productNameEn = intent.getStringExtra("productNameEn")
        val productNameSn = intent.getStringExtra("productNameSn")
        price = intent.getDoubleExtra("price", 0.0)
        val productImg = intent.getStringExtra("productImg")

        val productImageView = findViewById<ImageView>(R.id.cartProdImg)
        val productNameEnTextView = findViewById<TextView>(R.id.cartProdNameEn)
        val productNameSnTextView = findViewById<TextView>(R.id.cartProdNameSn)
        val priceTextView = findViewById<TextView>(R.id.cartProdPrice)

        val quantityTextView = findViewById<TextView>(R.id.qty)
        val qtyPlusButton = findViewById<FloatingActionButton>(R.id.qtyplus)

        productNameEnTextView.text = productNameEn
        productNameSnTextView.text = productNameSn
        priceTextView.text = price.toString()
        quantityTextView.text = quantity.toString()

        Glide.with(this)
            .load(productImg)
            .into(productImageView)

        val totalPriceTextView = findViewById<TextView>(R.id.totprice)
        // Calculate the initial total price
        val initialTotal = quantity * price
        totalPriceTextView.text = String.format("%.2f", initialTotal)
        // Add a click listener to the floating action button
        qtyPlusButton.setOnClickListener {
            quantity++
            quantityTextView.text = quantity.toString()
            val total = quantity * price

            val totalPriceTextView = findViewById<TextView>(R.id.totprice)
            totalPriceTextView.text = String.format("%.2f", total)
        }



        // Navbar
        val sessionManager = SessionManager(this)
        val userEmail = sessionManager.getUserEmail()

        val db = FirebaseFirestore.getInstance()

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
    }
}
