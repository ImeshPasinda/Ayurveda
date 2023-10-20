package com.example.ayurveda

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.squareup.picasso.Picasso

class DoctorProfile : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_doctor_profile)






        val backButton = findViewById<ImageButton>(R.id.backtoHomebtn)
        backButton.setOnClickListener {
            val intent = Intent(this, Home::class.java)
            startActivity(intent)
        }

        val userNavBtn = findViewById<ImageButton>(R.id.userProfileNavbtn)
        userNavBtn.setOnClickListener {
            val intent = Intent(this, UserProfile::class.java)
            startActivity(intent)
        }


        val docNameEn = intent.getStringExtra("docNameEn")
        val avatarUrl = intent.getStringExtra("avatarUrl")
        val docQualification = intent.getStringExtra("docQualification")
        val docLicense = intent.getStringExtra("docLicense")
        val docPhoneNo = intent.getStringExtra("docPhoneNo")
        val docAddress = intent.getStringExtra("docAddress")
        val longitude = intent.getDoubleExtra("longitude", 0.0)
        val latitude = intent.getDoubleExtra("latitude", 0.0)


        val bookNowBtn = findViewById<Button>(R.id.booknowbtndocprof) // Replace with the actual ID of your button

        bookNowBtn.setOnClickListener {
            val intent = Intent(this, DoctorBooking::class.java) // Replace "Booking" with the actual name of your booking activity

            // Pass the docNameEn value to the Booking activity
            intent.putExtra("docNameEn", docNameEn)

            // Start the Booking activity
            startActivity(intent)
        }

        val docNameEnTextView = findViewById<TextView>(R.id.docNamePtxt)
        val avatarImageView = findViewById<ImageView>(R.id.avatarP)
        val qualificationTextView = findViewById<TextView>(R.id.userName)
        val licenseTextView = findViewById<TextView>(R.id.subuserName)
        val addressTextView = findViewById<TextView>(R.id.subuserPassword)

        docNameEnTextView.text = docNameEn
        qualificationTextView.text = docQualification
        licenseTextView.text = docLicense
        addressTextView.text = docAddress

        val phoneNoTextView = findViewById<TextView>(R.id.subuserEmail)
        phoneNoTextView.text = docPhoneNo

        phoneNoTextView.setOnClickListener {
            // Create an Intent to make a phone call
            val intent = Intent(Intent.ACTION_DIAL)

            // Set the phone number to be dialed
            intent.data = Uri.parse("tel:$docPhoneNo")

            // Check if there's an app to handle this intent
            if (intent.resolveActivity(packageManager) != null) {
                startActivity(intent)
            } else {
                // Handle the case where no app can handle the intent
                Toast.makeText(this, "No app found to make a phone call", Toast.LENGTH_SHORT).show()
            }
        }


        Picasso.get().load(avatarUrl).into(avatarImageView)

        val mapbtn = findViewById<ImageButton>(R.id.mapbtn)

        mapbtn.setOnClickListener {
            // Create an Intent to start the Map Activity
            val intent = Intent(this, Map::class.java)

            // Add latitude and longitude as extras to the Intent
            intent.putExtra("latitude", latitude)
            intent.putExtra("longitude", longitude)
            intent.putExtra("docAddress", docAddress)
            intent.putExtra("docNameEn", docNameEn)
            intent.putExtra("avatarUrl", avatarUrl)


            // Start the Map Activity
            startActivity(intent)
        }
    }
}