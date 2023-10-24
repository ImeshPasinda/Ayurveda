package com.example.ayurveda

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.FirebaseFirestore

class AddDoctorRemedy : AppCompatActivity() {
    private lateinit var titleRemedyEditText: EditText
    private lateinit var categoryRemedyEditText: EditText
    private lateinit var descriptionRemedyEditText: EditText
    private lateinit var addRemedyButton: Button

    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_doctor_remedy)

        // Initialize UI elements
        titleRemedyEditText = findViewById(R.id.titleRemedy)
        descriptionRemedyEditText = findViewById(R.id.desRemedy)
        addRemedyButton = findViewById(R.id.addRemedybtn)


        // Inside your onCreate method
        val categoryRemedySpinner = findViewById<Spinner>(R.id.cateRemedy)
        val categories = arrayOf("Sneezing", "Headache", "Cough", "Fever")

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, categories)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        categoryRemedySpinner.adapter = adapter


        val sessionManager = SessionManager(this)
        val userEmail = sessionManager.getUserEmail()

        // Retrieve the username from the "users" collection
        val usersCollection = db.collection("doctors")
        usersCollection.whereEqualTo("email", userEmail)
            .get()
            .addOnSuccessListener { userQuerySnapshot ->
                if (!userQuerySnapshot.isEmpty) {
                    // There may be multiple documents matching the email; loop through them if needed
                    for (userDocument in userQuerySnapshot.documents) {
                        val username = userDocument.getString("docNameEn")

                        // Now you have the username, and you can use it as needed
                        // For example, you can set it in a TextView
                        val usernameTextView = findViewById<TextView>(R.id.unamenavbar)
                        usernameTextView.text = "Dr. " + (username?.split(" ")?.get(0) ?: "Doctor")

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

        addRemedyButton.setOnClickListener {
            // Retrieve the data from the EditText fields
            val title = titleRemedyEditText.text.toString()
            val description = descriptionRemedyEditText.text.toString()
            val selectedCategory = categoryRemedySpinner.selectedItem.toString()

            // Check if all fields are filled
            if (title.isNotEmpty() && description.isNotEmpty()) {
                // Fetch the doctor's email (you'll need to replace this with your actual method of getting the email)
                fun getDoctorName(userEmail: String): Task<String> {
                    // Fetch the doctorName from the "doctors" collection based on the doctor's email
                    return db.collection("doctors")
                        .whereEqualTo("email", userEmail)
                        .get()
                        .continueWith { task ->
                            if (task.isSuccessful) {
                                val doctorData = task.result.documents.firstOrNull()
                                if (doctorData != null) {
                                    doctorData.getString("docNameEn") ?: ""
                                } else {
                                    // Handle the case where the doctor's email is not found
                                    ""
                                }
                            } else {
                                // Handle the case where fetching the doctorName failed
                                ""
                            }
                        }
                }
                // Fetch the doctorName based on the doctor's email
                if (userEmail != null) {
                    getDoctorName(userEmail).addOnSuccessListener { doctorName ->
                        // Create a Map with the data to store in the "remedies" collection
                        val remedyData = hashMapOf(
                            "title" to title,
                            "category" to selectedCategory,
                            "description" to description,
                            "doctorName" to doctorName
                        )

                        // Add the data to the "remedies" collection in Firestore
                        db.collection("remedies")
                            .add(remedyData)
                            .addOnSuccessListener { documentReference ->
                                showToast("Remedy added successfully!")

                                val intent = Intent(this, DoctorRemedyAddSuccess::class.java)
                                startActivity(intent)
                            }
                            .addOnFailureListener { e ->
                                // Handle errors
                                showToast("Failed to add remedy: $e")
                            }
                    }
                }
            } else {
                showToast("Please fill in all fields")
            }
        }
    }



    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
