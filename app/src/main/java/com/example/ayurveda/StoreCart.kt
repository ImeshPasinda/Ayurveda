package com.example.ayurveda

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.bumptech.glide.Glide

class StoreCart : AppCompatActivity() {
    private var quantity = 1 // Initialize quantity to 1
    private var price = 0.0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_store_cart)

        val productNameEn = intent.getStringExtra("productNameEn")
        val productNameSn = intent.getStringExtra("productNameSn")
        price = intent.getDoubleExtra("price", 0.0)
        val productImg = intent.getStringExtra("productImg")

        val productImageView = findViewById<ImageView>(R.id.cartProdImg)
        val productNameEnTextView = findViewById<TextView>(R.id.cartProdNameEn)
        val productNameSnTextView = findViewById<TextView>(R.id.cartProdNameSn)
        val priceTextView = findViewById<TextView>(R.id.cartProdPrice)

        val quantityTextView = findViewById<TextView>(R.id.qty)
        val qtyPlusButton = findViewById<FloatingActionButton>(R.id.qtyplus)

        productNameEnTextView.text = productNameEn
        productNameSnTextView.text = productNameSn
        priceTextView.text = price.toString()
        quantityTextView.text = quantity.toString()

        Glide.with(this)
            .load(productImg)
            .into(productImageView)

        val totalPriceTextView = findViewById<TextView>(R.id.totprice)
        // Calculate the initial total price
        val initialTotal = quantity * price
        totalPriceTextView.text = String.format("%.2f", initialTotal)
        // Add a click listener to the floating action button
        qtyPlusButton.setOnClickListener {
            quantity++
            quantityTextView.text = quantity.toString()
            val total = quantity * price

            val totalPriceTextView = findViewById<TextView>(R.id.totprice)
            totalPriceTextView.text = String.format("%.2f", total)
        }
    }
}
