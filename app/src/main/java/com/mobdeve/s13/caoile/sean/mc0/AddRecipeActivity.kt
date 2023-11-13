package com.mobdeve.s13.caoile.sean.mc0

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class AddRecipeActivity : AppCompatActivity() {

    private lateinit var imgRecipe: ImageView
    private lateinit var btnUploadImage: Button
    private lateinit var etRecipeName: EditText
    private lateinit var etInstructions: EditText
    private lateinit var rvIngredients: RecyclerView
    private lateinit var btnAddIngredient: Button
    private lateinit var btnSaveRecipe: Button

    private lateinit var database: DatabaseReference

    private val ingredientsList = mutableListOf<IngredientModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_recipe)

        // Initialize Firebase
        database = FirebaseDatabase.getInstance().reference

        // Find views
        imgRecipe = findViewById(R.id.imgRecipe)
        btnUploadImage = findViewById(R.id.btnUploadImage)
        etRecipeName = findViewById(R.id.etRecipeName)
        etInstructions = findViewById(R.id.etInstructions)
        rvIngredients = findViewById(R.id.rvIngredients)
        btnAddIngredient = findViewById(R.id.btnAddIngredient)
        btnSaveRecipe = findViewById(R.id.btnSaveRecipe)

        // Set click listener for btnSaveRecipe
        btnSaveRecipe.setOnClickListener {
            saveRecipeToFirebase()
        }

        btnAddIngredient.setOnClickListener {
            addIngredientRow()
        }
    }

    private fun saveRecipeToFirebase() {
        // Get values from UI elements
        val recipeName = etRecipeName.text.toString()
        val instructions = etInstructions.text.toString()

        // Create a Recipe object
        val recipe = RecipeModel(recipeName, instructions, ingredientsList)

        // Get a unique key for the recipe
        val recipeKey = database.child("recipes").push().key

        // Save the recipe to the database
        database.child("recipes").child(recipeKey!!).setValue(recipe)

        // Finish the activity or navigate to another screen
        finish()
    }

    private fun addIngredientRow() {
        val layoutInflater = LayoutInflater.from(this)
        val ingredientRowView = layoutInflater.inflate(R.layout.add_ingredient_item, null, false)

        // Find views in the ingredientRowView
        val etIngredientName = ingredientRowView.findViewById<EditText>(R.id.ingredientName)
        val etQuantity = ingredientRowView.findViewById<EditText>(R.id.quantity)
        val etMeasurement = ingredientRowView.findViewById<EditText>(R.id.measurement)

        // Get values from the ingredientRowView
        val ingredientName = etIngredientName.text.toString()
        val quantity = etQuantity.text.toString()
        val measurement = etMeasurement.text.toString()

        // Create an IngredientModel object
        val ingredient = IngredientModel(ingredientName, quantity, measurement)

        // Add the ingredient to the list
        ingredientsList.add(ingredient)

        // TODO: Add logic to display the ingredientRowView in your layout
        // Example: linearLayout.addView(ingredientRowView)

        // Clear the input fields
        etIngredientName.text.clear()
        etQuantity.text.clear()
        etMeasurement.text.clear()
    }
}
