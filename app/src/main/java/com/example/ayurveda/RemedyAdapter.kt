package com.example.ayurveda

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ayurveda.R
import com.example.ayurveda.Remedy

class RemedyAdapter(private var remediesList: List<Remedy>) :
    RecyclerView.Adapter<RemedyAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item_remedies, parent, false)
        return ViewHolder(view)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val remedy = remediesList[position]

        // Bind data to the views
        holder.remedyTitle.text = remedy.title
        holder.remedyDocName.text = remedy.doctorName

        holder.profileBtn.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, Remedies::class.java)

            intent.putExtra("description", remedy.description)
            intent.putExtra("doctorName", remedy.doctorName)
            intent.putExtra("title", remedy.title)

            context.startActivity(intent)
        }
    }

    fun updateData(newData: List<Remedy>) {
        remediesList = newData
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return remediesList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val remedyTitle: TextView = itemView.findViewById(R.id.remedyTitle)
        val remedyDocName: TextView = itemView.findViewById(R.id.remedyDocName)
        val profileBtn: ImageButton = itemView.findViewById(R.id.goRemedyProfile)
    }
}
