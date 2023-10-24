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
            if (documentId.isNotEmpty()) {
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
                    }
            }
        }
        db.collection("appointments")
            .whereEqualTo("patientName", patientName)
            .whereEqualTo("bookingDate", bookingDate)
            .whereEqualTo("bookingTime", bookingTime)
            .get()
            .addOnSuccessListener { documents ->
                if (!documents.isEmpty) {
                    // Assuming there is only one matching document
                    documentId = documents.documents[0].id
                }
            }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

}