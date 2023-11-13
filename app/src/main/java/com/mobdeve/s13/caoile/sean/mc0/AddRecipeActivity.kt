package com.mobdeve.s13.caoile.sean.mc0

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputEditText

class AddRecipeActivity : AppCompatActivity() {

    private lateinit var ingredientsAdapter: IngredientsAdapter // Create a custom adapter
    private lateinit var etNewIngredient: TextInputEditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_recipe)

        // Initialize your RecyclerView and adapter
        val rvIngredients: RecyclerView = findViewById(R.id.rvIngredients)
        ingredientsAdapter = IngredientsAdapter()
        rvIngredients.layoutManager = LinearLayoutManager(this)
        rvIngredients.adapter = ingredientsAdapter

        // Set up the click listener for the add ingredient button
        val btnAddIngredient: Button = findViewById(R.id.btnAddIngredient)
        btnAddIngredient.setOnClickListener {
            addIngredient()
        }

        // Set up the click listener for the save button
        val btnSaveRecipe: Button = findViewById(R.id.btnSaveRecipe)
        btnSaveRecipe.setOnClickListener {
            saveRecipe()
        }
    }

    private fun saveRecipe() {
        // Your existing save recipe logic

        // You can get all the ingredient items from the adapter
        val ingredientList = ingredientsAdapter.getIngredients()

        // Now you have the list of ingredients in the ingredientList variable
        // You can use this list to save to Firebase or perform any other actions
    }

    private fun addIngredient() {
        val newIngredient = etNewIngredient.text.toString().trim()
        if (newIngredient.isNotEmpty()) {
            ingredientsAdapter.addIngredient(newIngredient)
            etNewIngredient.text = null
        }
    }
}
