package com.example.ayurveda

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.TextView
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class WelcomeDoctor : AppCompatActivity() {
    val db = Firebase.firestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome_doctor)

        val sessionManager = SessionManager(this)
        val userEmail = sessionManager.getUserEmail()

        // Retrieve the 'docNameEn' from the "doctors" collection based on the doctor's ID
        val doctorsCollection = db.collection("doctors")
        doctorsCollection.whereEqualTo("email",userEmail)
            .get()
            .addOnSuccessListener { userQuerySnapshot ->
                if (!userQuerySnapshot.isEmpty) {
                    val doctorDocument = userQuerySnapshot.documents[0]
                    val docNameEn = doctorDocument.getString("docNameEn")

                // Now you have the 'docNameEn' value, and you can set it in the TextView
                val docNameTextView = findViewById<TextView>(R.id.weldocname)

                    docNameTextView.text = docNameEn?.split(" ")?.get(0) ?: "Doc.Name"
                }
            }
            .addOnFailureListener { exception ->
                // Handle the failure to retrieve the doctor's data
                Log.e("Firestore", "Error getting doctor document: $exception")
            }
       if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
           val window = window
            window.statusBarColor = getColor(R.color.white)
        }

       val btnLetsGo = findViewById<ImageButton>(R.id.letsgobtn)
        btnLetsGo.setOnClickListener {
           val intent = Intent(this, DoctorProfileOwn::class.java)
            startActivity(intent)
        }
    }
    override fun onBackPressed() {
        // Leave this method empty to block the back button
    }
}