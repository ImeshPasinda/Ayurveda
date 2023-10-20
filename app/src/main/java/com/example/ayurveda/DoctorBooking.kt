package com.example.ayurveda

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatDelegate
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class DoctorBooking : AppCompatActivity() {

    // Initialize Firestore
    val db = Firebase.firestore

    @SuppressLint("MissingInflatedId", "WrongViewCast")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_doctor_booking)

        // Retrieve the value of docNameEn from the intent
        val docNameEn = intent.getStringExtra("docNameEn")

        // Find the TextView in your "Booking" activity's layout
        val bookingDocNameTextView =
            findViewById<TextView>(R.id.docName) // Replace with the actual ID

        // Set the value of docNameEn in the TextView
        bookingDocNameTextView.text = docNameEn

        // Retrieve the Spinner view by its ID
        val timesSpinner = findViewById<Spinner>(R.id.timesspinner)

        // Create an ArrayAdapter using the string array and a default Spinner layout
        val items = arrayOf(
            "පෙ.ව 8.30 - පෙ.ව 9.00",
            "පෙ.ව 9.30 - පෙ.ව 10.00",
            "පෙ.ව 10.30 - පෙ.ව 11.00",
            "ප.ව 2.30 - ප.ව 3.00",
            "ප.ව 3.30 - ප.ව 4.00",
            "ප.ව 4.30 - ප.ව 5.00"
        ) // Replace with your desired items
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, items)

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        // Apply the adapter to the spinner
        timesSpinner.adapter = adapter

        // Find the EditText by its ID
        val datepickerDisplayEditText = findViewById<EditText>(R.id.datepickerbooking)

        // Set a click listener to show the DatePickerDialog
        datepickerDisplayEditText.setOnClickListener {
            val calendar = Calendar.getInstance()

            val datePickerDialog = DatePickerDialog(
                this,
                DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                    val selectedDate = Calendar.getInstance()
                    selectedDate.set(year, monthOfYear, dayOfMonth)

                    val monthAbbreviation =
                        SimpleDateFormat("MMM", Locale.US).format(selectedDate.time)
                    val day = selectedDate.get(Calendar.DAY_OF_MONTH)
                    val year = selectedDate.get(Calendar.YEAR)

                    val daySuffix = getDayWithSuffix(day)
                    val formattedDate = "$daySuffix $monthAbbreviation $year"

                    datepickerDisplayEditText.setText(formattedDate)
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            )

            datePickerDialog.show()
        }

        // Navbar
        val sessionManager = SessionManager(this)
        val userEmail = sessionManager.getUserEmail()

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        val userNavButton = findViewById<ImageButton>(R.id.userProfileNavbtn)
        userNavButton.setOnClickListener {
            val intent = Intent(this, UserProfile::class.java)
            startActivity(intent)
        }
        // Retrieve the username from the "users" collection
        val usersCollection = db.collection("users")
        usersCollection.whereEqualTo("email", userEmail)
            .get()
            .addOnSuccessListener { userQuerySnapshot ->
                if (!userQuerySnapshot.isEmpty) {
                    // There may be multiple documents matching the email; loop through them if needed
                    for (userDocument in userQuerySnapshot.documents) {
                        val username = userDocument.getString("username")

                        // Now you have the username, and you can use it as needed
                        // For example, you can set it in a TextView
                        val usernameTextView = findViewById<TextView>(R.id.unamenavbar)
                        usernameTextView.text = username?.split(" ")?.get(0) ?: "User"

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



        val doctorBookButton = findViewById<Button>(R.id.doctorbookbtn)


        doctorBookButton.setOnClickListener {
            val bookingDate = datepickerDisplayEditText.text.toString()
            val bookingTime = timesSpinner.selectedItem.toString()

            if (bookingDate.isNotEmpty() && bookingTime.isNotEmpty()) {
                // Date and time are selected, proceed to Doctor Payment
                val intent = Intent(this, DoctorPayment::class.java)
                intent.putExtra("bookingDate", bookingDate)
                intent.putExtra("bookingTime", bookingTime)
                intent.putExtra("doctorName", docNameEn)
                startActivity(intent)
            } else {
                // Show a toast message or handle the case when date or time is not selected
                Toast.makeText(this, "Please select a date and time.", Toast.LENGTH_SHORT).show()
            }
        }

    }
    // Function to add "st," "nd," "rd," or "th" suffix to day
    fun getDayWithSuffix(day: Int): String {
        return when (day % 10) {
            1 -> "${day}st"
            2 -> "${day}nd"
            3 -> "${day}rd"
            else -> "${day}th"
        }
    }
}
