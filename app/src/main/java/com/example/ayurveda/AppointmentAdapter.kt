package com.example.ayurveda

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class AppointmentAdapter(private val appointments: List<AppointmentDoc>) : RecyclerView.Adapter<AppointmentAdapter.AppointmentViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppointmentViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_appoinments_pending, parent, false)
        return AppointmentViewHolder(view)
    }

    override fun onBindViewHolder(holder: AppointmentViewHolder, position: Int) {
        val appointment = appointments[position]
        holder.bind(appointment)


        // Find the ImageButton inside the item layout (replace R.id.your_button_id)
        val goDoctorProfileButton = holder.itemView.findViewById<ImageButton>(R.id.goDoctorProfile)

        // Set an OnClickListener for the button
        goDoctorProfileButton.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, DoctorDiagnosisForm::class.java)
            intent.putExtra("patientName", appointment.patientName)
            intent.putExtra("bookingDate", appointment.bookingDate)
            intent.putExtra("bookingTime", appointment.bookingTime)
          // You can add data to the intent if needed
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return appointments.size
    }

    inner class AppointmentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val pNameTextView: TextView = itemView.findViewById(R.id.pNamepending)
        private val pDateTextView: TextView = itemView.findViewById(R.id.pDatepending)
        private val pTimeTextView: TextView = itemView.findViewById(R.id.pTimepending)

        fun bind(appointment: AppointmentDoc) {
            pNameTextView.text = appointment.patientName
            pDateTextView.text = appointment.bookingDate
            pTimeTextView.text = appointment.bookingTime
        }
    }

}
