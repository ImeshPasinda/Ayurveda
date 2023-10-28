package com.example.ayurveda

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.io.ByteArrayOutputStream

class AddProduct : AppCompatActivity() {

    // Declare your UI elements
    private lateinit var proaddNameEn: EditText
    private lateinit var proaddNameSn: EditText
    private lateinit var proaddDesc: EditText
    private lateinit var proaddPrice: EditText
    private lateinit var productAddbtn: Button
    private lateinit var proAddCategorySpinner: Spinner
    private lateinit var preViewImg: ImageView
    private lateinit var selectProImgbtn: FloatingActionButton


    // Firebase Firestore and Storage
    private val db = FirebaseFirestore.getInstance()
    private val productsCollection = db.collection("products")
    private val storage = FirebaseStorage.getInstance()
    private val storageRef = storage.reference.child("product_images")

    private var imageUri: Uri? = null
    private lateinit var userEmail: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_product)




        // Navbar
        val sessionManager = SessionManager(this)
        userEmail = sessionManager.getUserEmail().toString()


        val db = FirebaseFirestore.getInstance()

        val userNavButton = findViewById<ImageButton>(R.id.userProfileNavbtn)
        userNavButton.setOnClickListener {
            val intent = Intent(this, UserProfile::class.java)
            startActivity(intent)
        }

        val usersCollection = db.collection("users")
        usersCollection.whereEqualTo("email", userEmail)
            .get()
            .addOnSuccessListener { userQuerySnapshot ->
                if (!userQuerySnapshot.isEmpty) {
                    for (userDocument in userQuerySnapshot.documents) {
                        val username = userDocument.getString("username")

                        val usernameTextView = findViewById<TextView>(R.id.unamenavbar)
                        usernameTextView.text = username?.split(" ")?.get(0) ?: "User"

                        break
                    }
                } else {
                    Log.d("Firestore", "No user document found for email: $userEmail")
                }
            }
            .addOnFailureListener { exception ->
                Log.e("Firestore", "Error getting user document: $exception")
            }




















        // Initialize UI elements by their IDs
        productAddbtn = findViewById(R.id.proaddBtn)
        proaddNameEn = findViewById(R.id.proaddNameEn)
        proaddNameSn = findViewById(R.id.proaddNameSn)
        proaddDesc = findViewById(R.id.proaddDesc)
        proaddPrice = findViewById(R.id.proaddPrice)
        proAddCategorySpinner = findViewById(R.id.proaddCategory)
        preViewImg = findViewById(R.id.preViewImg)
        selectProImgbtn = findViewById(R.id.selectproImgbtn)

        val categoryItems = arrayOf("Sneezing", "Headache", "Cough", "Fever")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, categoryItems)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        proAddCategorySpinner.adapter = adapter

        selectProImgbtn.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            startActivityForResult(intent, IMAGE_PICK_CODE)
        }

        productAddbtn.setOnClickListener {
            val nameEn = proaddNameEn.text.toString()
            val nameSn = proaddNameSn.text.toString()
            val description = proaddDesc.text.toString()
            val price = proaddPrice.text.toString().toDoubleOrNull()
            val category = proAddCategorySpinner.selectedItem.toString()

            if (nameEn.isNotEmpty() && nameSn.isNotEmpty() && description.isNotEmpty() && price != null && imageUri != null) {
                // All fields are filled, and an image is selected
                uploadImageToStorage(imageUri!!)
            } else {
                // Display an error message
                showToast("Please fill in all fields!")
            }

        }
    }




    private fun uploadImageToStorage(imageUri: Uri) {
        val imageRef = storageRef.child("${System.currentTimeMillis()}_${imageUri.lastPathSegment}")
        val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, imageUri)
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val data = baos.toByteArray()

        val uploadTask = imageRef.putBytes(data)
        uploadTask.addOnSuccessListener { taskSnapshot ->
            imageRef.downloadUrl.addOnSuccessListener { uri ->
                val imageUrl = uri.toString()
                saveProductToFirestore(imageUrl)
            }
        }.addOnFailureListener { e ->
            showToast("Failed to upload image. Error: ${e.message}")
        }
    }


    private fun saveProductToFirestore(imageUrl: String?) {
        val nameEn = proaddNameEn.text.toString()
        val nameSn = proaddNameSn.text.toString()
        val description = proaddDesc.text.toString()
        val price = proaddPrice.text.toString().toDoubleOrNull()
        val category = proAddCategorySpinner.selectedItem.toString()


        if (nameEn.isNotEmpty() && nameSn.isNotEmpty() && description.isNotEmpty() && price != null) {
            val productMap = hashMapOf(
                "productNameEn" to nameEn,
                "productNameSn" to nameSn,
                "description" to description,
                "price" to price,
                "category" to category,
                "productImg" to imageUrl,
                "sellerEmail" to userEmail

            )

            productsCollection.add(productMap)
                .addOnSuccessListener { documentReference ->
                    val productId = documentReference.id
                    showToast("Product added successfully!")
                    // Navigate to SuccessActivity and pass data
                    val successIntent = Intent(this, ProductAddedStatus::class.java)
                    successIntent.putExtra("refNo", productId)
                    startActivity(successIntent)
                }
                .addOnFailureListener { e ->
                    showToast("Failed to add product. Error: ${e.message}")
                }
        }
    }

    companion object {
        private const val IMAGE_PICK_CODE = 1000
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == IMAGE_PICK_CODE && resultCode == Activity.RESULT_OK) {
            imageUri = data?.data
            preViewImg.setImageURI(imageUri)
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}