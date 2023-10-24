package com.example.ayurveda

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText

class EditDiagnosisForm : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_diagnosis_form)

        // Retrieve values from the intent
        val symptoms = intent.getStringExtra("symptoms") ?: ""
        val medication = intent.getStringExtra("medication") ?: ""
        val notes = intent.getStringExtra("notes") ?: ""

        // Initialize EditText fields
        val symptomsEditText = findViewById<EditText>(R.id.cardno)
        val medicationEditText = findViewById<EditText>(R.id.medication)
        val notesEditText = findViewById<EditText>(R.id.notes)

        // Set the retrieved values as default text in EditText fields
        symptomsEditText.setText(symptoms)
        medicationEditText.setText(medication)
        notesEditText.setText(notes)
    }
}
