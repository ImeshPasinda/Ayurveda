package com.example.ayurveda

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class StoreAdapter(private val items: List<StoreItem>) : RecyclerView.Adapter<StoreAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val itemTextView: TextView = itemView.findViewById(R.id.textView18)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_store_products, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.itemTextView.text = item.name
    }

    override fun getItemCount(): Int {
        return items.size
    }
}
