package com.example.ayurveda

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore

class UsersRegister : AppCompatActivity() {
    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_users_register)


        val btnSignIn = findViewById<ImageButton>(R.id.signinbtn)

        btnSignIn.setOnClickListener {
            val intent = Intent(this, UsersLogin::class.java)
            startActivity(intent)
        }

        val btnDocReg = findViewById<ImageButton>(R.id.regasdoctor)

        btnDocReg.setOnClickListener {
            val intent = Intent(this, DoctorRegister::class.java)
            startActivity(intent)
        }

        firestore = FirebaseFirestore.getInstance()

        val btnUsersRegister = findViewById<Button>(R.id.userregisterbtn)
        val editTextUsername = findViewById<EditText>(R.id.uname)
        val editTextEmail = findViewById<EditText>(R.id.uemail)
        val editTextPassword = findViewById<EditText>(R.id.upassword)
        val editTextConfirmPassword = findViewById<EditText>(R.id.urepassword)

        btnUsersRegister.setOnClickListener {
            // Clear previous errors
            editTextUsername.error = null
            editTextEmail.error = null
            editTextPassword.error = null
            editTextConfirmPassword.error = null

            // Get user input values
            val username = editTextUsername.text.toString().trim()
            val email = editTextEmail.text.toString().trim()
            val password = editTextPassword.text.toString()
            val confirmPassword = editTextConfirmPassword.text.toString()

            var hasError = false

            // Check for empty fields
            if (username.isEmpty()) {
                editTextUsername.error = "Username is required"
                hasError = true
            }

            val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"

            if (email.isEmpty()) {
                editTextEmail.error = "Email is required"
                hasError = true
            } else if (!email.matches(emailPattern.toRegex())) {
                editTextEmail.error = "Invalid email format"
                hasError = true
            }

            if (password.isEmpty()) {
                editTextPassword.error = "Password is required"
                hasError = true
            }

            if (confirmPassword.isEmpty()) {
                editTextConfirmPassword.error = "Please confirm your password"
                hasError = true
            }

            if (hasError) {
                return@setOnClickListener
            }

            // Check for password match
            if (password != confirmPassword) {
                editTextConfirmPassword.error = "Passwords do not match"
                return@setOnClickListener
            }

            // Check if the email already exists in the database
            firestore.collection("users")
                .whereEqualTo("email", email)
                .get()
                .addOnSuccessListener { documents ->
                    if (documents.isEmpty) {
                        // Email is not in use, so we can proceed to add the user
                        val userData = hashMapOf(
                            "username" to username,
                            "email" to email,
                            "password" to password
                        )

                        firestore.collection("users")
                            .add(userData)
                            .addOnSuccessListener { documentReference ->
                                val sessionManager = SessionManager(this)
                                sessionManager.saveUserSession(email)
                                val intent = Intent(this, WelcomeUser::class.java)
                                startActivity(intent)
                                finish()
                            }
                            .addOnFailureListener { e ->
                                editTextUsername.error = "Failed to register user: ${e.message}"
                            }
                    } else {
                        editTextEmail.error = "Email is already in use"
                    }
                }
                .addOnFailureListener { e ->
                    editTextUsername.error = "Failed to check email availability: ${e.message}"
                }
        }

    }
}
