package com.example.ayurveda

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class YourProductAdapter(private val productList: List<Product>) : RecyclerView.Adapter<YourProductAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_your_products, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val product = productList[position]
        holder.bind(product)

    }

    override fun getItemCount(): Int {
        return productList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val productImageView: ImageView = itemView.findViewById(R.id.youprodimg)
        // Bind data to the views in the item layout
        fun bind(product: Product) {
            itemView.findViewById<TextView>(R.id.youProdEn).text = product.productNameEn
            itemView.findViewById<TextView>(R.id.youProdSn).text = product.productNameSn
            itemView.findViewById<TextView>(R.id.youProdprice).text = product.price.toString()


            // Load and display the product image using Glide
            Glide.with(itemView.context)
                .load(product.productImg)
                .into(productImageView)

        }
    }
}
