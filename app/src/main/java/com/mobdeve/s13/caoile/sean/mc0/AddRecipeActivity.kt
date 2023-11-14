package com.mobdeve.s13.caoile.sean.mc0

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import android.net.Uri
import android.provider.MediaStore
import android.view.View
import android.widget.ImageButton
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts


class AddRecipeActivity : AppCompatActivity(), ImageUploadCallback {

    private lateinit var imgRecipe: ImageView
    private lateinit var btnUploadImage: Button
    private lateinit var etRecipeName: EditText
    private lateinit var etInstructions: EditText
    private lateinit var llIngredients: LinearLayout
    private lateinit var btnAddIngredient: Button
    private lateinit var btnSaveRecipe: Button
    private lateinit var imageUri: Uri
    private lateinit var btnBack: ImageButton

    private lateinit var getContent: ActivityResultLauncher<String>

    private lateinit var ingredientsList: ArrayList<IngredientModel>
    private val database = FirebaseFirestore.getInstance()
    private val recipesRef = database.collection("recipes")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_recipe)

        val username = intent.getStringExtra("USERNAME_KEY").toString()

        // Find views
        imgRecipe = findViewById(R.id.imgRecipe)
        btnUploadImage = findViewById(R.id.btnUploadImage)
        etRecipeName = findViewById(R.id.etRecipeName)
        etInstructions = findViewById(R.id.etInstructions)
        btnAddIngredient = findViewById(R.id.btnAddIngredient)
        btnSaveRecipe = findViewById(R.id.btnSaveRecipe)
        llIngredients = findViewById(R.id.llIngredientsContainer)
        btnBack = findViewById(R.id.backBtn)

        val imageUploadCallback = object : ImageUploadCallback {
            override fun onImageUploaded(imageURL: String) {
                // Implement your logic upon successful image upload here
                saveRecipeToFirebase(username, imageURL)
            }

            override fun onImageUploadFailed(exception: Exception) {
                // Handle the failure scenario if image upload fails
                Log.e("AddRecipeActivity", "Error uploading image to Firebase Storage", exception)
            }
        }

        btnBack.setOnClickListener{
            finish()
        }
        // Set click listener for btnSaveRecipe
        btnSaveRecipe.setOnClickListener {
            // Call the function to upload the image
            uploadImageToFirebaseStorage(imageUri, imageUploadCallback)
        }


        // Set click listener for btnAddIngredient
        btnAddIngredient.setOnClickListener {
            Log.d("HELLO", "HELLO")
            addRowDynamically()
        }

        getContent = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                imageUri = it
                imgRecipe.setImageURI(it) // Display the selected image in an ImageView (imgRecipe)
            }
        }

        btnUploadImage.setOnClickListener {
            getContent.launch("image/*")
        }
    }

    private fun uploadImageToFirebaseStorage(imageUri: Uri, callback: ImageUploadCallback) {
        val storageRef = FirebaseStorage.getInstance().reference
        val imageRef = storageRef.child("images/${System.currentTimeMillis()}.jpg")

        imageRef.putFile(imageUri)
            .addOnSuccessListener { taskSnapshot ->
                // Image uploaded successfully, get the download URL
                imageRef.downloadUrl.addOnSuccessListener { uri ->
                    // Now you have the download URL, you can use it as imageURL in saveRecipeToFirebase
                    Log.d("AddedIMG", "SUCCESS")
                    val imageURL = uri.toString()
                    callback.onImageUploaded(imageURL)
                }
            }
            .addOnFailureListener { e ->
                Log.e("AddRecipeActivity", "Error uploading image to Firebase Storage", e)
                // Handle the error if the image upload fails
                callback.onImageUploadFailed(e)
            }

    }

    private fun saveRecipeToFirebase(username: String, imageURL: String) {
        // Get values from UI elements
        val recipeName = etRecipeName.text.toString()
        val instructions = etInstructions.text.toString()
        val ingredientsList = getIngredientsFromLayout()
        val imageURI = imageURL
        // Create a Recipe object

        val recipeDB = hashMapOf(
            "creator" to username,
            "imageURI" to imageURL,
            "ingredients" to ingredientsList,
            "instructions" to instructions,
            "name" to recipeName
        )
        Log.d("Name", recipeName)
        Log.d("Instruc", instructions)

        recipesRef.add(recipeDB)
            .addOnSuccessListener { documentReference ->
                Log.d("AddRecipeActivity", "Recipe added with ID: ${documentReference.id}")
                // Finish the activity or navigate to another screen
                finish()
            }
            .addOnFailureListener { e ->
                Log.e("AddRecipeActivity", "Error adding recipe", e)
                // Handle the error, if necessary
            }
    }

    private fun getIngredientsFromLayout(): ArrayList<IngredientModel> {
        val ingredients = ArrayList<IngredientModel>()

        // Iterate through each child of llIngredients
        for (i in 0 until llIngredients.childCount) {
            val rowView = llIngredients.getChildAt(i)
            val etIngredientName = rowView.findViewById<EditText>(R.id.ingredientName)
            val etQuantity = rowView.findViewById<EditText>(R.id.quantity)
            val etMeasurement = rowView.findViewById<EditText>(R.id.measurement)

            val ingredient = IngredientModel(
                etIngredientName.text.toString(),
                etQuantity.text.toString().toFloat(),
                etMeasurement.text.toString()
            )

            ingredients.add(ingredient)
        }

        return ingredients
    }

    private fun addRowDynamically() {
        val inflater = LayoutInflater.from(this)
        val rowView = inflater.inflate(R.layout.add_ingredient_item, null)

        val btnDelete = rowView.findViewById<Button>(R.id.btnDelete)
        btnDelete.setOnClickListener {
            val parentView = btnDelete.parent as View
            // Remove the parent view (ingredient row) from llIngredients LinearLayout
            llIngredients.removeView(parentView)
        }

        llIngredients.addView(rowView)
    }

    override fun onImageUploaded(imageURL: String) {
        TODO("Not yet implemented")
    }

    override fun onImageUploadFailed(exception: Exception) {
        TODO("Not yet implemented")
    }

}
