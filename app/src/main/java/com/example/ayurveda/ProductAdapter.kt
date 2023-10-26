package com.example.ayurveda

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.floatingactionbutton.FloatingActionButton

class ProductAdapter(private var productList: List<Product>) : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    private var categoryFilter: String? = null

    fun updateData(newProducts: List<Product>, category: String? = null) {
        productList = newProducts
        categoryFilter = category
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_store_products, parent, false)
        return ProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = productList[position]
        holder.productNameEnTextView.text = product.productNameEn
        holder.productNameSnTextView.text = product.productNameSn
        holder.priceTextView.text = product.price.toString()

        // Load and display the product image using Glide
        Glide.with(holder.itemView.context)
            .load(product.productImg)
            .into(holder.productImageView)

        holder.addCartBtn.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, ProductsView::class.java)

            intent.putExtra("productNameEn", product.productNameEn)
            intent.putExtra("productNameSn", product.productNameSn)
            intent.putExtra("price", product.price)
            intent.putExtra("productImg", product.productImg)
            intent.putExtra("productDescription", product.description)

            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return productList.size
    }

    inner class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val productImageView: ImageView = itemView.findViewById(R.id.prodImg)
        val productNameEnTextView: TextView = itemView.findViewById(R.id.prodNameEn)
        val productNameSnTextView: TextView = itemView.findViewById(R.id.prodNameSn)
        val priceTextView: TextView = itemView.findViewById(R.id.prodPrice)
        val addCartBtn: FloatingActionButton = itemView.findViewById(R.id.addcartbtn)
    }
}