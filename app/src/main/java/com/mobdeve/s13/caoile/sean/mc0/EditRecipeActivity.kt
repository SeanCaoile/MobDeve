package com.mobdeve.s13.caoile.sean.mc0

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.firebase.firestore.FirebaseFirestore

class EditRecipeActivity : AppCompatActivity() {

    companion object{
        const val NAME_KEY = "NAME_KEY"
        const val IMG_KEY = "IMG_KEY"
    }

    private lateinit var etFoodName: TextView
    private lateinit var food_creatorTv: TextView
    private lateinit var llIngredientsContainer: LinearLayout
    private lateinit var etIinstructions: EditText
    private lateinit var recipeImg: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_recipe)

        etFoodName = findViewById(R.id.etFoodName)
        food_creatorTv = findViewById(R.id.food_creatorTv)
        llIngredientsContainer = findViewById(R.id.llIngredientsContainer)
        etIinstructions = findViewById(R.id.etIinstructions)
        recipeImg = findViewById(R.id.recipeImg)

        val recipeName = intent.getStringExtra(EditRecipeActivity.NAME_KEY) // Replace with your recipe name

        val db = FirebaseFirestore.getInstance()
        val recipeRef = db.collection("recipes")
        val query = recipeRef.whereEqualTo("name", recipeName)


        query.get()
            .addOnSuccessListener { documents ->
                if (!documents.isEmpty()) {
                    val document = documents.documents[0]
                    val imageURL = document.getString("imageURI") // Get the image URL
                    val creator = document.getString("creator") // Get the creator
                    // Get other recipe details similarly
                    Glide.with(this)
                        .load(imageURL)
                        .into(recipeImg)
                    // Set the retrieved data to the respective UI elements
                    etFoodName.text = recipeName
                    food_creatorTv.text = creator
                    // Set other data to respective UI elements

                    // For ingredients
                    val ingredients = document.get("ingredients") as? ArrayList<HashMap<String, Any>>?
                    // Loop through ingredients and display them
                    ingredients?.forEach { ingredientData ->
                        val name = ingredientData["ingredient"] as? String ?: ""
                        val quantity = ingredientData["quantity"] as? Number ?: ""
                        val measurement = ingredientData["measurement"] as? String ?: ""

                        val inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
                        val rowView = inflater.inflate(R.layout.add_ingredient_item, null)

                        val ingredientName = rowView.findViewById<EditText>(R.id.ingredientName)
                        val quantityField = rowView.findViewById<EditText>(R.id.quantity)
                        val measurementField = rowView.findViewById<EditText>(R.id.measurement)
                        val deleteBtn = rowView.findViewById<Button>(R.id.btnDelete)

                        ingredientName.setText(name)
                        quantityField.setText(quantity.toString())
                        measurementField.setText(measurement)

                        llIngredientsContainer.addView(rowView)

                        deleteBtn.setOnClickListener {
                            llIngredientsContainer.removeView(rowView)
                        }
                    }

                    // For instructions
                    val instructions = document.getString("instructions")
                    etIinstructions.setText(instructions)
                } else {
                    // Handle the case where no documents were found
                }
            }
            .addOnFailureListener { exception ->
                // Handle any errors
            }

        val confirmButton = findViewById<Button>(R.id.btnConfirm)
        confirmButton.setOnClickListener {
            if (recipeName != null) {
                val db = FirebaseFirestore.getInstance()
                val recipeRef = db.collection("recipes")

                recipeRef.get()
                    .addOnSuccessListener { documents ->
                        for (document in documents) {
                            if (document.getString("name") == recipeName) {
                                val updatedRecipeRef = db.collection("recipes").document(document.id)

                                val sharedPrefs = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
                                val editor = sharedPrefs.edit()
                                editor.putString("docId", document.id)
                                editor.apply()
                                Log.d("docid", document.id)

                                val imageURL = document.getString("imageURI")

                                val updatedName = etFoodName.text.toString()
                                val updatedCreator = food_creatorTv.text.toString()
                                val updatedInstructions = etIinstructions.text.toString()
                                val updatedIngredients = ArrayList<HashMap<String, Any>>()

                                // Collect updated ingredients from llIngredientsContainer
                                for (i in 0 until llIngredientsContainer.childCount) {
                                    val rowView = llIngredientsContainer.getChildAt(i)
                                    val ingredientName = rowView.findViewById<EditText>(R.id.ingredientName).text.toString()
                                    val quantity = rowView.findViewById<EditText>(R.id.quantity).text.toString().toFloatOrNull() ?: 0f
                                    val measurement = rowView.findViewById<EditText>(R.id.measurement).text.toString()

                                    val ingredientMap = HashMap<String, Any>()
                                    ingredientMap["ingredient"] = ingredientName
                                    ingredientMap["quantity"] = quantity
                                    ingredientMap["measurement"] = measurement

                                    updatedIngredients.add(ingredientMap)
                                }

                                // Update the Firestore document with the new data
                                updatedRecipeRef.update(
                                    "name", updatedName,
                                    "creator", updatedCreator,
                                    "instructions", updatedInstructions,
                                    "ingredients", updatedIngredients
                                )
                                    .addOnSuccessListener {
                                        // Handle success

                                        finish()
                                    }
                                    .addOnFailureListener { exception ->
                                        // Handle failure
                                    }
                            }
                        }
                    }
                    .addOnFailureListener { exception ->
                        // Handle any errors
                    }
            }
        }

        val deleteButton = findViewById<Button>(R.id.btnDelete)
        deleteButton.setOnClickListener {
            val db = FirebaseFirestore.getInstance()
            val recipeRef = db.collection("recipes")

            recipeRef.get()
                .addOnSuccessListener { documents ->
                    for (document in documents) {
                        if (document.getString("name") == recipeName) {
                            val recipeDocRef = db.collection("recipes").document(document.id)

                            val sharedPrefs = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
                            val editor = sharedPrefs.edit()
                            editor.putString("docId", "deleted")
                            editor.apply()

                            // Delete the document from Firestore
                            recipeDocRef.delete()
                                .addOnSuccessListener {
                                    // Handle successful deletion
                                    Log.d("Delete", "DocumentSnapshot successfully deleted!")
                                    finish()
                                }
                                .addOnFailureListener { e ->
                                    // Handle unsuccessful deletion
                                    Log.w("Delete", "Error deleting document", e)
                                }
                        }
                    }
                }
                .addOnFailureListener { exception ->
                    // Handle any errors
                }
        }
    }

}
