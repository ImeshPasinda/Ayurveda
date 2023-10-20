package com.example.ayurveda

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CompletedAppointmentAdapter(private val appointments: List<Appointment>) :
    RecyclerView.Adapter<CompletedAppointmentAdapter.AppointmentViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppointmentViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_user_completed_appointments, parent, false)
        return AppointmentViewHolder(view)
    }

    override fun onBindViewHolder(holder: AppointmentViewHolder, position: Int) {
        val appointment = appointments[position]
        holder.doctorNameTextView.text = appointment.doctorName
        holder.bookingTimeTextView.text = appointment.bookingTime
        holder.bookingDateTextView.text = appointment.bookingDate
    }

    override fun getItemCount(): Int {
        return appointments.size
    }

    inner class AppointmentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val doctorNameTextView: TextView = itemView.findViewById(R.id.appDoneDocName)
        val bookingTimeTextView: TextView = itemView.findViewById(R.id.appDoneDocTime)
        val bookingDateTextView: TextView = itemView.findViewById(R.id.appDoneDocDate)
    }
}

