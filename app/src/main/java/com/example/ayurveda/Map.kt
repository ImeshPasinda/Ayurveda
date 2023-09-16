package com.example.ayurveda

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.example.ayurveda.databinding.ActivityMapBinding
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.Marker
import com.squareup.picasso.Picasso

class Map : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

    }


    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap


        val latitude = intent.getDoubleExtra("latitude", 0.0) // 0.0 is the default value if not found
        val longitude = intent.getDoubleExtra("longitude", 0.0)
        val docAddress = intent.getStringExtra("docAddress")
        val docNameEn = intent.getStringExtra("docNameEn")
        val avatarUrl = intent.getStringExtra("avatarUrl")

        // Add a marker in your desired location
        val location = LatLng(latitude, longitude)
        // Create a BitmapDescriptor from the image resource
        val markerBitmap = BitmapFactory.decodeResource(resources, R.drawable.mapmarker)
        val resizedMarkerBitmap = Bitmap.createScaledBitmap(markerBitmap, 100, 100, false)
        val markerIcon = BitmapDescriptorFactory.fromBitmap(resizedMarkerBitmap)

        val markerOptions = MarkerOptions()
            .position(location)
            .title(docNameEn)
            .snippet(docAddress)
            .icon(markerIcon)
            .anchor(0.5f, 0.5f) // Center the marker on the specified position


        // Add the marker to the map
        val marker = mMap.addMarker(markerOptions)

        // Set a custom info window adapter for the marker
        mMap.setInfoWindowAdapter(object : GoogleMap.InfoWindowAdapter {
            override fun getInfoWindow(marker: Marker): View? {
                // Return null to use the default info window
                return null
            }

            override fun getInfoContents(marker: Marker): View {
                // Inflate your custom info window layout
                val view = layoutInflater.inflate(R.layout.mapcardview, null)

                // Find the ImageView and TextView in the layout
                val avatarImageView = view.findViewById<ImageView>(R.id.avatarMap)
                val descriptionTextView = view.findViewById<TextView>(R.id.doctorDesMap)
                val titleTextView = view.findViewById<TextView>(R.id.doctorNameMap)

                // Set the image and description
                Picasso.get().load(avatarUrl).into(avatarImageView)
                descriptionTextView.text = marker.snippet
                titleTextView.text = marker.title

                return view
            }
        })

        // Set the default zoom level
        val zoomLevel = 10f
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, zoomLevel))
    }




}