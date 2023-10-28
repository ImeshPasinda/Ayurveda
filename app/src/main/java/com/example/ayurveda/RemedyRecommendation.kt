package com.example.ayurveda


import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.SearchView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore

class RemedyRecommendation : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var remedyArrayList: ArrayList<Remedy>
    private lateinit var remedyAdapter: RemedyAdapter
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_remedy_recommendation)

        // Back to DoctorRecommendation
        val backButton = findViewById<ImageButton>(R.id.backbtn)
        backButton.setOnClickListener {
            val intent = Intent(this, DoctorRecommendation::class.java)
            startActivity(intent)
        }

        // Profile
        db = FirebaseFirestore.getInstance()
        val sessionManager = SessionManager(this)
        val userEmail = sessionManager.getUserEmail()

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

        // Doctor
        val doctorsBtn = findViewById<Button>(R.id.doctorsbtn)
        doctorsBtn.setOnClickListener {
            val intent = Intent(this, DoctorRecommendation::class.java)
            startActivity(intent)
        }

        // Herb
        val herbsBtn = findViewById<Button>(R.id.herbsbtn)
        herbsBtn.setOnClickListener {
            val intent = Intent(this, HerbRecommendation::class.java)
            startActivity(intent)
        }

        // Appointments
        val appBtn = findViewById<ImageButton>(R.id.userAppointmentsbtn)
        appBtn.setOnClickListener {
            val intent = Intent(this, UserAppointments::class.java)
            startActivity(intent)
        }

        // Store
        val storeBtn = findViewById<ImageButton>(R.id.storenavbtn)
        storeBtn.setOnClickListener {
            val intent = Intent(this, StoreHome::class.java)
            startActivity(intent)
        }

        // Home
        val homeBtn = findViewById<ImageButton>(R.id.homenavbtn_1)
        homeBtn.setOnClickListener {
            val intent = Intent(this, Home::class.java)
            startActivity(intent)
        }

        recyclerView = findViewById(R.id.recycleviewremedies)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)

        remedyArrayList = ArrayList()
        remedyAdapter = RemedyAdapter(remedyArrayList)
        recyclerView.adapter = remedyAdapter

        db = FirebaseFirestore.getInstance()

        loadRemedies()

        // Initialize the SearchView and set up the search listener
        val searchView = findViewById<SearchView>(R.id.searchView)

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                // Handle search when the user submits a query
                performSearch(query)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                // Handle search as the user types
                performSearch(newText)
                return true
            }
        })
    }


    private fun loadRemedies() {
        db.collection("remedies").get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    val category = document.getString("category")
                    val description = document.getString("description")
                    val doctorName = document.getString("doctorName")
                    val title = document.getString("title")

                    if (category != null && description != null && doctorName != null &&
                        title != null
                    ) {
                        val remedy = Remedy(category, description, doctorName, title)
                        remedyArrayList.add(remedy)
                    }
                }
                remedyAdapter.notifyDataSetChanged()
            }
            .addOnFailureListener { exception ->
                Toast.makeText(this, exception.toString(), Toast.LENGTH_SHORT).show()
            }
    }

    private fun performSearch(query: String?) {
        val filteredRemedies = if (query.isNullOrBlank()) {
            // If the query is empty or null, show all remedies
            remedyArrayList
        } else {
            // Filter remedies based on the category field
            remedyArrayList.filter { remedy ->
                remedy.category.contains(query, ignoreCase = true)
            }
        }

        // Update the RecyclerView with the filtered data
        remedyAdapter.updateData(filteredRemedies)
    }

}
