package com.example.ayurveda
import android.annotation.SuppressLint
import com.google.firebase.firestore.FirebaseFirestore
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.content.Intent
import android.util.Log
import android.widget.ImageButton
import android.widget.Toast

class DoctorRegister : AppCompatActivity() {
    private val db = FirebaseFirestore.getInstance()

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_doctor_register)


        val signInButton = findViewById<ImageButton>(R.id.docsigninbtn)

        // Set an OnClickListener for the "Sign In" button
        signInButton.setOnClickListener {
            // Navigate to the DoctorLogin activity
            val intent = Intent(this, DoctorLogin::class.java)
            startActivity(intent)
        }

        val UsersignUpButton = findViewById<ImageButton>(R.id.userregbtnback)

        // Set an OnClickListener for the "Sign In" button
        UsersignUpButton.setOnClickListener {
            // Navigate to the DoctorLogin activity
            val intent = Intent(this, UsersRegister::class.java)
            startActivity(intent)
        }

        val unameEditText = findViewById<EditText>(R.id.uname)
        val urepasswordEditText = findViewById<EditText>(R.id.urepassword)
        val slmcregEditText = findViewById<EditText>(R.id.slmcreg)
        val specializationEditText = findViewById<EditText>(R.id.specialization)
        val upasswordEditText = findViewById<EditText>(R.id.upassword)
        val uemailEditText = findViewById<EditText>(R.id.uemail)
        val registerButton = findViewById<Button>(R.id.userregisterbtn)

        registerButton.setOnClickListener {
            val docNameEn = unameEditText.text.toString()
            val password = upasswordEditText.text.toString()
            val docLicense = slmcregEditText.text.toString()
            val docQualification = specializationEditText.text.toString()
            val email = uemailEditText.text.toString()




            // First, check if the email already exists in Firestore
            db.collection("doctors")
                .whereEqualTo("email", email)
                .get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val querySnapshot = task.result
                        if (querySnapshot != null && !querySnapshot.isEmpty) {
                            // Email already exists, show an error message
                            showToast("Email already in use. Please choose a different email.")
                        } else {
                            // Email is not a duplicate, proceed with registration
                            val userData = hashMapOf(
                                "docNameEn" to docNameEn,
                                "email" to email,
                                "docLicense" to docLicense,
                                "docQualification" to docQualification,
                                "password" to password,
                                "address" to null,
                                "avatarUrl" to "null",
                                "docNameSn" to null,
                                "docPhoneNo" to null,
                                "latitude" to null,
                                "longitude" to null,
                                "category" to null,
                            )

                            db.collection("doctors")
                                .add(userData)
                                .addOnSuccessListener { documentReference ->
                                    // Document added successfully
                                    val docId = documentReference.id
                                    Log.d("Firestore", "DocumentSnapshot added with ID: $docId")

                                    showToast("Registration successful!")

                                    // Navigate to the WelcomeDoctor activity
                                    val intent = Intent(this, DoctorOtherDetails::class.java)
                                    startActivity(intent)

                                    val sessionManager = SessionManager(this)
                                    sessionManager.saveUserSession(email)
                                }
                                .addOnFailureListener { e ->
                                    // Handle errors
                                    Log.w("Firestore", "Error adding document", e)
                                }

                        }
                    } else {
                        // Handle the case where checking for duplicate email failed
                        Log.e("Firestore", "Error checking for duplicate email", task.exception)
                    }
                }
        }

    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}