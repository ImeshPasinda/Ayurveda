package com.example.ayurveda

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore

import android.widget.SearchView
import android.widget.TextView

class HerbRecommendation : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var searchView: SearchView
    private lateinit var productAdapter: ProductAdapter
    private val allProducts: MutableList<Product> = mutableListOf()
    private var selectedCategory: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_herb_recommendation)

        //backToRemedyRecommend
        val backButton = findViewById<ImageButton>(R.id.backbtn)
        backButton.setOnClickListener {
            val intent = Intent(this, RemedyRecommendation::class.java)
            startActivity(intent)
        }

        //profile
        val userNavBtn = findViewById<ImageButton>(R.id.userProfileNavbtn)
        userNavBtn.setOnClickListener {
            val intent = Intent(this, UserProfile::class.java)
            startActivity(intent)
        }

        //remedy
        val remediesBtn = findViewById<Button>(R.id.remediesbtn)
        remediesBtn.setOnClickListener {
            val intent = Intent(this, RemedyRecommendation::class.java)
            startActivity(intent)
        }

        //doctor
        val doctorsBtn = findViewById<Button>(R.id.doctorsbtn)
        doctorsBtn.setOnClickListener {
            val intent = Intent(this, DoctorRecommendation::class.java)
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

        recyclerView = findViewById(R.id.store_recyclerview)
        searchView = findViewById(R.id.herbSearch)

        val db = FirebaseFirestore.getInstance()

        // Query your Firestore collection
        db.collection("products")
            .get()
            .addOnSuccessListener { querySnapshot ->
                allProducts.clear()

                for (document in querySnapshot) {
                    val product = document.toObject(Product::class.java)
                    allProducts.add(product)
                }

                setupRecyclerView(allProducts)
                setupSearchView(allProducts)
            }
            .addOnFailureListener { exception ->
                // Handle error
            }



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
    }

    private fun setupRecyclerView(products: List<Product>) {
        val layoutManager = GridLayoutManager(this, 2)
        recyclerView.layoutManager = layoutManager
        productAdapter = ProductAdapter(products)
        recyclerView.adapter = productAdapter
    }

    private fun setupSearchView(allProducts: List<Product>) {
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                selectedCategory = newText?.toLowerCase()
                val filteredProducts = filterProducts(allProducts, selectedCategory)
                productAdapter.updateData(filteredProducts)
                return true
            }
        })
    }

    private fun filterProducts(allProducts: List<Product>, category: String?): List<Product> {
        if (category.isNullOrBlank()) {
            return allProducts
        }

        val lowerCaseCategory = category.toLowerCase()

        return allProducts.filter { product ->
            val categoryMatch = product.category.toLowerCase().contains(lowerCaseCategory)
            categoryMatch
        }
    }
}