package com.mobdeve.s13.caoile.sean.mc0

//import android.os.Bundle
//import android.widget.EditText
//import android.widget.LinearLayout
//import android.widget.TextView
//import androidx.appcompat.app.AppCompatActivity
//import com.google.firebase.firestore.FirebaseFirestore
//
//class EditRecipeActivity : AppCompatActivity() {
//
//    companion object{
//        const val NAME_KEY = "NAME_KEY"
//    }
//
//    private lateinit var etFoodName: TextView
//    private lateinit var food_creatorTv: TextView
//    private lateinit var llIngredientsContainer: LinearLayout
//    private lateinit var etIinstructions: EditText
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_edit_recipe)
//
//        etFoodName = findViewById(R.id.etFoodName)
//        food_creatorTv = findViewById(R.id.food_creatorTv)
//        llIngredientsContainer = findViewById(R.id.llIngredientsContainer)
//        etIinstructions = findViewById(R.id.etIinstructions)
//
//        val recipeName = intent.getStringExtra(EditRecipeActivity.NAME_KEY) // Replace with your recipe name
//
//        val db = FirebaseFirestore.getInstance()
//        val recipeRef = db.collection("recipes")
//        val query = recipeRef.whereEqualTo("name", recipeName)
//
//        query.get()
//            .addOnSuccessListener { documents ->
//                for (document in documents) {
//                    val imageURL = document.getString("imageURI") // Get the image URL
//                    val creator = document.getString("creator") // Get the creator
//                    // Get other recipe details similarly
//
//                    // Set the retrieved data to the respective UI elements
//                    etFoodName.setText(recipeName)
//                    food_creatorTv.setText(creator)
//                    // Set other data to respective UI elements
//
//                    // For ingredients (assuming it's a list of strings)
//                    val ingredients = document.get("ingredients") as? List<String>
//                    // Loop through ingredients and display them
//                    if (ingredients != null) {
//                        for (ingredient in ingredients) {
//                            val textView = TextView(this)
//                            textView.text = ingredient
//                            llIngredientsContainer.addView(textView)
//                        }
//                    }
//
//                    // For instructions
//                    val instructions = document.getString("instructions")
//                    etIinstructions.setText(instructions)
//                }
//            }
//            .addOnFailureListener { exception ->
//                // Handle any errors
//            }
//    }
//}
