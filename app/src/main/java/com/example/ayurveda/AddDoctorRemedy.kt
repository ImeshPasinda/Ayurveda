package com.example.ayurveda

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import android.view.View
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
        categoryRemedyEditText = findViewById(R.id.cateRemedy)
        descriptionRemedyEditText = findViewById(R.id.desRemedy)
        addRemedyButton = findViewById(R.id.addRemedybtn)

        val sessionManager = SessionManager(this)
        val userEmail = sessionManager.getUserEmail()

        addRemedyButton.setOnClickListener {
            // Retrieve the data from the EditText fields
            val title = titleRemedyEditText.text.toString()
            val category = categoryRemedyEditText.text.toString()
            val description = descriptionRemedyEditText.text.toString()

            // Check if all fields are filled
            if (title.isNotEmpty() && category.isNotEmpty() && description.isNotEmpty()) {
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
                            "category" to category,
                            "description" to description,
                            "doctorName" to doctorName
                        )

                        // Add the data to the "remedies" collection in Firestore
                        db.collection("remedies")
                            .add(remedyData)
                            .addOnSuccessListener { documentReference ->
                                showToast("Remedy added with ID: ${documentReference.id}")
                                // Clear the EditText fields after adding the remedy
                                titleRemedyEditText.text.clear()
                                categoryRemedyEditText.text.clear()
                                descriptionRemedyEditText.text.clear()

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
