package com.example.ayurveda

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

class DoctorAdapter(private val doctorsList: List<Doctor>) :
    RecyclerView.Adapter<DoctorAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item_doctors, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val doctor = doctorsList[position]

        // Bind data to the views
        holder.docNameEn.text = doctor.docNameEn
        holder.docNameSn.text = doctor.docNameSn
        // Load the avatar image using a library like Picasso or Glide
        // Example using Picasso:
        //Picasso.get().load(doctor.avatarUrl).into(holder.avatarImageView)

        // Set click listener for the profile button if needed
        holder.profileButton.setOnClickListener {
            // Handle profile button click
        }
    }

    override fun getItemCount(): Int {
        return doctorsList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val docNameEn: TextView = itemView.findViewById(R.id.doctorNameEn)
        val docNameSn: TextView = itemView.findViewById(R.id.doctorNameSn)
        val avatarImageView: ImageView = itemView.findViewById(R.id.doctorAvatar)
        val profileButton: ImageButton = itemView.findViewById(R.id.goDoctorProfile)
    }
}
