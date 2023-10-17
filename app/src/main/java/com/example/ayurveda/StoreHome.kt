package com.example.ayurveda

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class StoreHome : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_store_home)

        // Find the RecyclerView by its ID
        val recyclerView: RecyclerView = findViewById(R.id.store_recyclerview)

        // Create sample data
        val sampleData = listOf(
            StoreItem("Paspanguwa"),
            StoreItem("Paspanguwa"),
            StoreItem("Item 3"),
            StoreItem("Item 4"),
            StoreItem("Item 4"),
            StoreItem("Item 4"),
            StoreItem("Item 4"),
            StoreItem("Item 4"),
            StoreItem("Item 4"),
            // Add more sample items as needed
        )

        // Create an adapter for the RecyclerView
        val adapter = StoreAdapter(sampleData)

        // Set the adapter to the RecyclerView
        recyclerView.adapter = adapter

        // Create a GridLayoutManager with two columns
        val layoutManager = GridLayoutManager(this, 2)

        // Set the GridLayoutManager on the RecyclerView
        recyclerView.layoutManager = layoutManager
    }
}

