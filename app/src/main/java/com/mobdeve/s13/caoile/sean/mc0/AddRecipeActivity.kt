package com.mobdeve.s13.caoile.sean.mc0

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
import android.net.Uri
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
    private var imgUploaded: Boolean = false

    private val database = FirebaseFirestore.getInstance()
    private val recipesRef = database.collection("recipes")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_recipe)

        val username = intent.getStringExtra("USERNAME_KEY").toString()

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
                saveRecipeToFirebase(username, imageURL)
            }

            override fun onImageUploadFailed(exception: Exception) {
                Log.e("AddRecipeActivity", "Error uploading image to Firebase Storage", exception)
            }
        }

        btnBack.setOnClickListener{
            finish()
        }
        btnSaveRecipe.setOnClickListener {
            if (!imgUploaded) {
                imgRecipe.setImageResource(R.drawable.no_img)
            } else {
                uploadImageToFirebaseStorage(imageUri, imageUploadCallback)
            }
        }

        btnAddIngredient.setOnClickListener {
            Log.d("HELLO", "HELLO")
            addRowDynamically()
        }

        getContent = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                imageUri = it
                imgRecipe.setImageURI(it)
            }
        }

        btnUploadImage.setOnClickListener {
            getContent.launch("image/*")
            imgUploaded = true
        }
    }

    private fun uploadImageToFirebaseStorage(imageUri: Uri, callback: ImageUploadCallback) {
        val storageRef = FirebaseStorage.getInstance().reference
        val imageRef = storageRef.child("images/${System.currentTimeMillis()}.jpg")

        imageRef.putFile(imageUri)
            .addOnSuccessListener { taskSnapshot ->
                imageRef.downloadUrl.addOnSuccessListener { uri ->
                    Log.d("AddedIMG", "SUCCESS")
                    val imageURL = uri.toString()
                    callback.onImageUploaded(imageURL)
                }
            }
            .addOnFailureListener { e ->
                Log.e("AddRecipeActivity", "Error uploading image to Firebase Storage", e)
                callback.onImageUploadFailed(e)
            }
    }

    private fun saveRecipeToFirebase(username: String, imageURL: String) {

        val recipeName = etRecipeName.text.toString()
        val instructions = etInstructions.text.toString()
        val ingredientsList = getIngredientsFromLayout()

        if (recipeName.isEmpty()) {
            etRecipeName.error = "Recipe name is empty"
        } else if (instructions.isEmpty()) {
            etInstructions.error = "Instructions are empty"
        } else {
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
                    finish()
                }
                .addOnFailureListener { e ->
                    Log.e("AddRecipeActivity", "Error adding recipe", e)
                }
        }
    }

    private fun getIngredientsFromLayout(): ArrayList<IngredientModel> {
        val ingredients = ArrayList<IngredientModel>()

        for (i in 0 until llIngredients.childCount) {
            val rowView = llIngredients.getChildAt(i)
            val etIngredientName = rowView.findViewById<EditText>(R.id.ingredientName)
            val etQuantity = rowView.findViewById<EditText>(R.id.quantity)
            val etMeasurement = rowView.findViewById<EditText>(R.id.measurement)

            if (etIngredientName.toString().isEmpty()){
                etIngredientName.error = "Ingredient cannot be empty"
            } else if (etQuantity.toString().isEmpty()){
                etQuantity.error = "Quantity cannot be empty"
            } else if (etMeasurement.toString().isEmpty()){
                etMeasurement.error = "Measurement cannot be empty"
            }
            else {
                val ingredient = IngredientModel(
                    etIngredientName.text.toString(),
                    etQuantity.text.toString().toFloat(),
                    etMeasurement.text.toString()
                )
                ingredients.add(ingredient)
            }
        }

        return ingredients
    }

    private fun addRowDynamically() {
        val inflater = LayoutInflater.from(this)
        val rowView = inflater.inflate(R.layout.add_ingredient_item, null)

        val btnDelete = rowView.findViewById<Button>(R.id.btnDelete)
        btnDelete.setOnClickListener {
            val parentView = btnDelete.parent as View
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
