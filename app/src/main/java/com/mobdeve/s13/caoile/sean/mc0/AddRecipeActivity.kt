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

class AddRecipeActivity : AppCompatActivity() {

    private lateinit var imgRecipe: ImageView
    private lateinit var btnUploadImage: Button
    private lateinit var etRecipeName: EditText
    private lateinit var etInstructions: EditText
    private lateinit var llIngredients: LinearLayout
    private lateinit var btnAddIngredient: Button
    private lateinit var btnSaveRecipe: Button

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


        // Set click listener for btnSaveRecipe
        btnSaveRecipe.setOnClickListener {
            saveRecipeToFirebase(username)
        }

        // Set click listener for btnAddIngredient
        btnAddIngredient.setOnClickListener {
            Log.d("HELLO", "HELLO")
            addRowDynamically()
        }
    }

    private fun saveRecipeToFirebase(username: String) {
        // Get values from UI elements
        val recipeName = etRecipeName.text.toString()
        val instructions = etInstructions.text.toString()
        val ingredientsList = getIngredientsFromLayout()
        val imageURL = "https://firebasestorage.googleapis.com/v0/b/mobdeve-s13--seancaoile.appspot.com/o/scrambledegg.png?alt=media&token=9280b7f7-eb0d-4570-93de-e51184cbcd12&_gl=1*8i2jt8*_ga*NTUxNzEzNDI5LjE2OTgwNjIzMzY.*_ga_CW55HF8NVT*MTY5OTMzOTYwNC4xNi4xLjE2OTkzNDE5OTAuNDIuMC4w"
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

        val btnConfirm = rowView.findViewById<Button>(R.id.btnConfirm)
        btnConfirm.setOnClickListener {
            // Handle confirmation logic for the dynamically added row
        }

        llIngredients.addView(rowView)
    }

}
