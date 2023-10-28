package com.example.ayurveda

import android.annotation.SuppressLint
import android.content.Intent
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.google.firebase.firestore.FirebaseFirestore
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.firestore.SetOptions

class DoctorDiagnosisForm : AppCompatActivity() {

    private lateinit var patientName: String
    private lateinit var bookingDate: String
    private lateinit var bookingTime: String

    private lateinit var symptomsEditText: EditText
    private lateinit var medicationEditText: EditText
    private lateinit var notesEditText: EditText
    private lateinit var saveButton: Button

    private val db = FirebaseFirestore.getInstance()
    private lateinit var documentId: String

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_doctor_diagnosis_form)

        // Retrieve data passed from the previous activity
        patientName = intent.getStringExtra("patientName") ?: ""
        bookingDate = intent.getStringExtra("bookingDate") ?: ""
        bookingTime = intent.getStringExtra("bookingTime") ?: ""

        val date = findViewById<TextView>(R.id.datetxtapp)
        date.text = bookingDate

        val name = findViewById<TextView>(R.id.unameappdoc)
        name.text = patientName

        // Initialize UI elements
        symptomsEditText = findViewById(R.id.cardno)
        medicationEditText = findViewById(R.id.medication)
        notesEditText = findViewById(R.id.notes)
        saveButton = findViewById(R.id.diagSavebtn)

        // Set a click listener for the "Save" button
        saveButton.setOnClickListener {
            // Get text from EditText fields
            val symptoms = symptomsEditText.text.toString()
            val medication = medicationEditText.text.toString()
            val notes = notesEditText.text.toString()

            // Prepare data to be stored in Firestore
            val diagnosisData = hashMapOf(
                "symptoms" to symptoms,
                "medication" to medication,
                "notes" to notes,
                "status" to "completed"
                // Add more fields as needed
            )

            // Check if there's an existing document ID
            if (documentId.isNotEmpty()) {
                // Update the document in the "appointments" collection
                db.collection("appointments")
                    .document(documentId)
                    .set(diagnosisData, SetOptions.merge())
                    .addOnSuccessListener {
                        // Handle successful data update
                        showToast("Diagnosis saved")
                        // You can show a success message or navigate to another activity
                        val intent = Intent(this, BookingCompleted::class.java)
                        intent.putExtra("symptoms", symptoms)
                        intent.putExtra("medication", medication)
                        intent.putExtra("notes", notes)
                        startActivity(intent)
                    }
                    .addOnFailureListener { e ->
                        // Handle errors
                        showToast("Failed to save diagnosis")
                    }
            }
        }

        // Query the Firestore database to find the matching document in the "appointments" collection
        db.collection("appointments")
            .whereEqualTo("patientName", patientName)
            .whereEqualTo("bookingDate", bookingDate)
            .whereEqualTo("bookingTime", bookingTime)
            .get()
            .addOnSuccessListener { documents ->
                if (!documents.isEmpty) {
                    // Assuming there is only one matching document
                    val document = documents.documents[0]
                    documentId = document.id

                    // Retrieve relevant data from the document and set it to the UI fields
                    val appointmentData = document.data
                    if (appointmentData != null) {
                        symptomsEditText.setText(appointmentData["symptoms"].toString())
                        medicationEditText.setText(appointmentData["medications"].toString())
                        notesEditText.setText(appointmentData["notes"].toString())
                    }
                }
            }
            .addOnFailureListener { e ->
                // Handle errors
                showToast("Failed to retrieve appointment data")
            }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
