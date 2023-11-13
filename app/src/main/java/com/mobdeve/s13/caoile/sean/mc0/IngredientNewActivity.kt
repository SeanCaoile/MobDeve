package com.mobdeve.s13.caoile.sean.mc0

import android.os.Bundle
import android.text.SpannableStringBuilder
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore

class IngredientNewActivity : AppCompatActivity(){
    private val database = FirebaseFirestore.getInstance()
    private val usersRef = database.collection("users")

    private lateinit var backBtn: ImageButton
    private lateinit var addBtn: Button
    private lateinit var nameET : EditText
    private lateinit var quantityET: EditText
    private lateinit var measurementET: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_ingredient)

        nameET = findViewById<View>(R.id.ingredientET) as EditText
        quantityET = findViewById<View>(R.id.quantityET) as EditText
        measurementET = findViewById<View>(R.id.measurementET) as EditText
        backBtn = findViewById<View>(R.id.backBtn) as ImageButton
        addBtn = findViewById<View>(R.id.addingredient) as Button

        val position = intent.getIntExtra(IngredientEditActivity.POSITION_KEY, 0)

        backBtn.setOnClickListener(View.OnClickListener {
            finish()
        })

        addBtn.setOnClickListener(View.OnClickListener {
            val ingredientName = nameET.text.toString()
            val newQuantity = quantityET.text.toString().toFloat()
            val newMeasurement = measurementET.text.toString()

            addIngredient(ingredientName, newQuantity, newMeasurement) { success ->
                if (success) {
                    finish()
                } else {
                    showToast("Failed to add Ingredient")
                }
            }
        })
    }

    private fun addIngredient(ingredientName: String, quantity: Float, measurement: String, callback: (Boolean) -> Unit)  {
        val sharedPrefs = getSharedPreferences("AppPrefs", MODE_PRIVATE)
        val currUser = sharedPrefs.getString("username","DEFAULT").toString()

        val document = usersRef.whereEqualTo("username", currUser).limit(1)

        document.get().addOnSuccessListener { documents ->
            for (document in documents) {
                val documentId = document.id
                val ingredientsArray = document.get("ingredient list") as? ArrayList<HashMap<String, Any>> ?: emptyList()

                if (ingredientsArray != null) {
                    var ingredientExists = false

                    for (ingredient in ingredientsArray) {
                        val ingredientField = ingredient["ingredient"]
                        if (ingredientField == ingredientName) {
                            ingredientExists = true
                            break
                        }
                    }
                    if (!ingredientExists) {
                        val newIngredient = mapOf(
                            "ingredient" to ingredientName,
                            "quantity" to quantity,
                            "measurement" to measurement
                        )
                        val newArray = ingredientsArray + newIngredient
                        usersRef.document(documentId).update("ingredient list", newArray)
                            .addOnSuccessListener {
                                // Update successfu l
                                println("Array updated successfully")
                                showToast("New ${ingredientName} Ingredient Added")

                                callback(true)
                            }
                            .addOnFailureListener { e ->
                                // Handle error
                                println("Error updating array: $e")
                                showToast("Failed to add Ingredient")

                                callback(false)
                            }
                    }
                }
            }
        }
        .addOnFailureListener { e ->
            // Handle failure
            // Log or display an error message
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}