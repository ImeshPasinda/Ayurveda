package com.example.ayurveda

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class UserProfile : AppCompatActivity() {

    // Initialize Firestore
    val db = Firebase.firestore
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_profile)

        val sessionManager = SessionManager(this)
        val userEmail = sessionManager.getUserEmail()




        val userNavButton = findViewById<ImageButton>(R.id.homenavbtn_1)
        userNavButton.setOnClickListener {
            val intent = Intent(this, Home::class.java)
            startActivity(intent)
        }

        val supplierbtnButton = findViewById<Button>(R.id.switchtosupplierbtn)
        supplierbtnButton.setOnClickListener {
            val intent = Intent(this, YourProducts::class.java)
            startActivity(intent)
        }



        // Retrieve the username from the "users" collection
        val usersCollection = db.collection("users")
        usersCollection.whereEqualTo("email", userEmail)
            .get()
            .addOnSuccessListener { userQuerySnapshot ->
                // Assuming there's only one document with the matching email
                if (!userQuerySnapshot.isEmpty) {
                    val userDocument = userQuerySnapshot.documents[0]
                    val username = userDocument.getString("username")
                    val useremail = userDocument.getString("email")
                    val userpassword = userDocument.getString("password")
                    // Now you have the username, and you can use it as needed
                    // For example, you can set it in a TextView

                    val usernavTextView = findViewById<TextView>(R.id.unamenavbar)
                    usernavTextView.text = username?.split(" ")?.get(0) ?: "User"

                    val usernameTextView = findViewById<TextView>(R.id.userName)
                    usernameTextView.text = username

                    val usersubnameTextView = findViewById<TextView>(R.id.subuserName)
                    usersubnameTextView.text = username

                    val useremailextView = findViewById<TextView>(R.id.subuserEmail)
                    useremailextView.text = useremail

                    val userpasswordextView = findViewById<TextView>(R.id.subuserPassword)
                    userpasswordextView.text = userpassword
                }
            }
            .addOnFailureListener { exception ->
                // Handle the failure to retrieve user data
                Log.e("Firestore", "Error getting user document: $exception")
            }

        val logoutButton = findViewById<Button>(R.id.userlogout_btn)
        logoutButton.setOnClickListener {
            // Logout action
            sessionManager.logout()
            redirectToLoginActivity()
        }
    }

    private fun redirectToLoginActivity() {
        // Create an Intent to navigate to the LoginActivity
        val intent = Intent(this, UsersLogin::class.java)
        // Clear the back stack so that the user cannot navigate back to UserProfile
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }
}
