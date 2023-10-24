package com.example.ayurveda

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import com.google.firebase.firestore.FirebaseFirestore
import android.widget.Toast
import com.google.firebase.firestore.SetOptions

class DoctorOtherDetails : AppCompatActivity() {
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_doctor_other_details)

        val addressEditText = findViewById<EditText>(R.id.addressdoc)
        val nameSindocEditText = findViewById<EditText>(R.id.nameSindoc)
        val phonedocEditText = findViewById<EditText>(R.id.phonedoc)
        val latdocEditText = findViewById<EditText>(R.id.latdoc)
        val londocEditText = findViewById<EditText>(R.id.londoc)
        val saveButton = findViewById<Button>(R.id.usersavebtn)

        val sessionManager = SessionManager(this)
        val userEmail = sessionManager.getUserEmail()

        // Inside your onCreate method
        val categoryDoctorSpinner = findViewById<Spinner>(R.id.doccategory)
        val categories = arrayOf("Sneezing", "Headache", "Cough", "Fever")

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, categories)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        categoryDoctorSpinner.adapter = adapter


        saveButton.setOnClickListener {
            val db = FirebaseFirestore.getInstance()

            val intent = Intent(this, WelcomeDoctor::class.java)
            startActivity(intent)

            db.collection("doctors")
                .whereEqualTo("email", userEmail)
                .get()
                .addOnSuccessListener { querySnapshot ->
                    if (querySnapshot.isEmpty) {
                        showToast("Doctor with the specified email doesn't exist.")
                    } else {
                        val userDoc = querySnapshot.documents[0]
                        val userId = userDoc.id

                        val address = addressEditText.text.toString()
                        val nameInSinhala = nameSindocEditText.text.toString()
                        val phoneNo = phonedocEditText.text.toString()
                        val latitudeText = latdocEditText.text.toString()
                        val longitudeText = londocEditText.text.toString()
                        val selectedCategory = categoryDoctorSpinner.selectedItem.toString()

                        if (address.isEmpty() || nameInSinhala.isEmpty() || phoneNo.isEmpty() || latitudeText.isEmpty() || longitudeText.isEmpty()) {
                            showToast("Please fill in all fields.")
                        } else {
                            val latitude = latitudeText.toDoubleOrNull()
                            val longitude = longitudeText.toDoubleOrNull()

                            if (latitude != null && longitude != null) {
                                val dataToUpdate = mapOf(
                                    "address" to address,
                                    "docNameSn" to nameInSinhala,
                                    "docPhoneNo" to phoneNo,
                                    "latitude" to latitude,
                                    "longitude" to longitude,
                                    "category" to selectedCategory
                                )

                                db.collection("doctors")
                                    .document(userId)
                                    .set(dataToUpdate, SetOptions.merge())
                                    .addOnSuccessListener {
                                        showToast("Data updated successfully.")
                                    }
                                    .addOnFailureListener { e ->
                                        showToast("Failed to update data: ${e.message}")
                                    }
                            } else {
                                showToast("Latitude and longitude must be valid numbers.")
                            }
                        }
                    }
                }
                .addOnFailureListener { e ->
                    showToast("Failed to retrieve user information: ${e.message}")
                }
        }
    }

    fun showToast(message: String) {
        Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
    }
}
