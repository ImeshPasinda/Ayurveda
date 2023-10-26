package com.example.ayurveda

import RemedyAdapter
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore

class RemedyRecommendation : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var remedyArrayList: ArrayList<Remedy>
    private lateinit var remedyAdapter: RemedyAdapter
    private lateinit var db: FirebaseFirestore
    private lateinit var searchView: SearchView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_remedy_recommendation)

        //backToDoctorRecommend
        val backButton = findViewById<ImageButton>(R.id.backbtn)
        backButton.setOnClickListener {
            val intent = Intent(this, DoctorRecommendation::class.java)
            startActivity(intent)
        }

        //profile
        val userNavBtn = findViewById<ImageButton>(R.id.userProfileNavbtn)
        userNavBtn.setOnClickListener {
            val intent = Intent(this, UserProfile::class.java)
            startActivity(intent)
        }

        //doctor
        val doctorsBtn = findViewById<Button>(R.id.doctorsbtn)
        doctorsBtn.setOnClickListener {
            val intent = Intent(this, DoctorRecommendation::class.java)
            startActivity(intent)
        }

        //herb
        val herbsBtn = findViewById<Button>(R.id.herbsbtn)
        herbsBtn.setOnClickListener {
            val intent = Intent(this, HerbRecommendation::class.java)
            startActivity(intent)
        }

        //appoinments
        val appBtn = findViewById<ImageButton>(R.id.imageButton8)
        appBtn.setOnClickListener {
            val intent = Intent(this, UserAppointments::class.java)
            startActivity(intent)
        }

        //store
        val storeBtn = findViewById<ImageButton>(R.id.imageButton9)
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



        recyclerView = findViewById(R.id.recycleviewremedies)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)

        remedyArrayList = ArrayList()
        remedyAdapter = RemedyAdapter(remedyArrayList)
        recyclerView.adapter = remedyAdapter

        db = FirebaseFirestore.getInstance()

        searchView = findViewById(R.id.searchView)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                remedyAdapter.filter(newText ?: "")
                return true
            }
        })

        loadRemedies()
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
}