package com.example.ayurveda
import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore

private val db = FirebaseFirestore.getInstance()
class ViewAppoinments : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var appointmentAdapter: AppointmentAdapter
    private val appointments = mutableListOf<AppointmentDoc>()
    private lateinit var doctorName: String // Declare doctorName at a higher scope

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_appoinments)

        val sessionManager = SessionManager(this)
        val userEmail = sessionManager.getUserEmail()

        // Reference to the "doctors" collection
        val doctorsCollection = db.collection("doctors")

        // Query the Firestore collection for the doctor with the specified email
        doctorsCollection.whereEqualTo("email", userEmail)
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    // Retrieve the doctor's name
                    doctorName = document.getString("docNameEn") ?: ""

                    if (doctorName.isNotEmpty()) {
                        // Do something with the doctor's name
                        println("Doctor's Name: $doctorName")

                        // After obtaining the doctor's name, you can use it in the query
                        val appointmentsCollection = db.collection("appointments")
                        val query = appointmentsCollection.whereEqualTo("doctorName", doctorName)

                        query.get()
                            .addOnSuccessListener { appointmentDocuments ->
                                for (appointmentDocument in appointmentDocuments) {
                                    val bookingDate = appointmentDocument.getString("bookingDate") ?: ""
                                    val patientName = appointmentDocument.getString("patientName") ?: ""
                                    val bookingTime = appointmentDocument.getString("bookingTime") ?: ""

                                    val appointment = AppointmentDoc(bookingDate, patientName, bookingTime)
                                    appointments.add(appointment)
                                }
                                appointmentAdapter.notifyDataSetChanged()
                            }
                            .addOnFailureListener { e ->
                                // Handle any errors that occurred during the fetch
                                Log.e(TAG, "Error getting appointment documents: ", e)
                            }
                    } else {
                        // Handle the case where the "docNameEn" field is not found
                        println("Doctor's name not found for $userEmail")
                    }
                }
            }
            .addOnFailureListener { exception ->
                // Handle any errors
                Log.e(TAG, "Error getting doctor's name: $exception")
            }

        recyclerView = findViewById(R.id.recycleviewpendingapp)
        recyclerView.layoutManager = LinearLayoutManager(this)
        appointmentAdapter = AppointmentAdapter(appointments)
        recyclerView.adapter = appointmentAdapter


    }
}
