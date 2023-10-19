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
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.bumptech.glide.Glide
import com.google.firebase.firestore.FirebaseFirestore

class StoreCart : AppCompatActivity() {
    private var quantity = 1
    private var price = 0.0
    private var total = 0.0

    @SuppressLint("MissingInflatedId")
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
        total = quantity * price
        totalPriceTextView.text = String.format("%.2f", total)

        qtyPlusButton.setOnClickListener {
            quantity++
            quantityTextView.text = quantity.toString()
            total = quantity * price

            val totalPriceTextView = findViewById<TextView>(R.id.totprice)
            totalPriceTextView.text = String.format("%.2f", total)
        }

        val qtyMinusButton = findViewById<FloatingActionButton>(R.id.qtyminus)

        qtyMinusButton.setOnClickListener {
            if (quantity > 1) {
                quantity--
                quantityTextView.text = quantity.toString()
                total = quantity * price

                val totalPriceTextView = findViewById<TextView>(R.id.totprice)
                totalPriceTextView.text = String.format("%.2f", total)
            }
        }

        val checkoutButton = findViewById<Button>(R.id.checkoutbtn)

        checkoutButton.setOnClickListener {
            val intent = Intent(this, StorePayment::class.java)
            intent.putExtra("productNameEn", productNameEn)
            intent.putExtra("productNameSn", productNameSn)
            intent.putExtra("productImg", productImg)
            intent.putExtra("totalPrice", total)

            startActivity(intent)
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

        val usersCollection = db.collection("users")
        usersCollection.whereEqualTo("email", userEmail)
            .get()
            .addOnSuccessListener { userQuerySnapshot ->
                if (!userQuerySnapshot.isEmpty) {
                    for (userDocument in userQuerySnapshot.documents) {
                        val username = userDocument.getString("username")

                        val usernameTextView = findViewById<TextView>(R.id.unamenavbar)
                        usernameTextView.text = username?.split(" ")?.get(0) ?: "User"

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
