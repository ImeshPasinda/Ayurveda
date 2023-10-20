package com.example.ayurveda

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot

class DoctorRecommendation : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var doctorArrayList: ArrayList<Doctor>
    private lateinit var doctorAdapter: DoctorAdapter
    private lateinit var db: FirebaseFirestore



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_doctor_recommendation)

//        val backbutton = findViewById<ImageButton>(com.google.firebase.firestore.R.id.doctorbackbtn)
//        backbutton.setOnClickListener{
//            val Intent = Intent(this,Home::class.java)
//            startActivity(Intent)
//        }

        recyclerView = findViewById<RecyclerView>(R.id.recycleviewdoctors)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)

        doctorArrayList = arrayListOf()

        doctorAdapter = DoctorAdapter(doctorArrayList)

        recyclerView.adapter = doctorAdapter



        db = FirebaseFirestore.getInstance()

        db.collection("doctors").get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    val docNameEn = document.getString("docNameEn")
                    val docNameSn = document.getString("docNameSn")
                    val avatarUrl = document.getString("avatarUrl")
                    val docQualification = document.getString("docQualification")
                    val docLicense = document.getString("docLicense")
                    val docPhoneNo = document.getString("docPhoneNo")
                    val address = document.getString("address")
                    val longitude = document.getDouble("longitude")
                    val latitude = document.getDouble("latitude")
//

                    if (docNameEn != null && docNameSn != null && avatarUrl != null &&
                        docQualification != null && docLicense != null && docPhoneNo != null &&
                        address != null && longitude != null && latitude != null ) {
                        val doctor = Doctor(
                            docNameEn,
                            docNameSn,
                            avatarUrl,
                            docQualification,
                            docLicense,
                            docPhoneNo,
                            address,
                            longitude,
                            latitude
                        )
                        doctorArrayList.add(doctor)
                    }
                }
                recyclerView.adapter = DoctorAdapter(doctorArrayList)
            }
            .addOnFailureListener { exception ->
                Toast.makeText(this, exception.toString(), Toast.LENGTH_SHORT).show()
            }


    }


}
