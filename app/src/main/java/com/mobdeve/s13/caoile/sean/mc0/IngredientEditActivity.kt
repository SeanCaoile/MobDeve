package com.mobdeve.s13.caoile.sean.mc0

import android.content.Context
import android.location.GnssMeasurement
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import org.w3c.dom.Text

class IngredientEditActivity : AppCompatActivity(){
    private val database = FirebaseFirestore.getInstance()
    private val usersRef = database.collection("users")
    companion object{
        const val NAME_KEY = "NAME_KEY"
        const val QUANTITY_KEY = "QUANTITY_KEY"
        const val TYPE_KEY = "TYPE_KEY"
        const val POSITION_KEY = "POSITION_KEY"
    }

    private lateinit var nameET : TextView
    private lateinit var quantityET: EditText
    private lateinit var measurementET: EditText
    private lateinit var backBtn: ImageButton
    private lateinit var deleteBtn: Button
    private lateinit var confirmBtn: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_ingredient)

        nameET = findViewById<View>(R.id.ingredientName_tv) as TextView
        quantityET = findViewById<View>(R.id.quantityET) as EditText
        measurementET = findViewById<View>(R.id.measurementET) as EditText
        backBtn = findViewById<View>(R.id.backBtn) as ImageButton
        deleteBtn = findViewById<View>(R.id.deleteBtn) as Button
        confirmBtn = findViewById<View>(R.id.confirmBtn) as Button

        nameET.text = SpannableStringBuilder(intent.getStringExtra(IngredientEditActivity.NAME_KEY))
        quantityET.text = SpannableStringBuilder(intent.getStringExtra(IngredientEditActivity.QUANTITY_KEY))
        measurementET.text = SpannableStringBuilder(intent.getStringExtra(IngredientEditActivity.TYPE_KEY))

        val position = intent.getIntExtra(IngredientEditActivity.POSITION_KEY, 0)

        backBtn.setOnClickListener(View.OnClickListener {
            finish()
        })

        deleteBtn.setOnClickListener(View.OnClickListener {
            val ingredientName = nameET.text.toString()
            deleteIngredient(ingredientName) { success ->
                if (success) {
                    finish()
                } else {
                    showToast("Failed to delete Ingredient")
                }
            }
        })

        confirmBtn.setOnClickListener(View.OnClickListener {
            val ingredientName = nameET.text.toString()
            val newQuantity = quantityET.text.toString()
            val newMeasurement = measurementET.text.toString()

            if (ingredientName.isNotEmpty() && newQuantity.isNotEmpty() && newMeasurement.isNotEmpty()) {
                updateIngredient(ingredientName, newQuantity.toFloat(), newMeasurement) { success ->
                    if (success) {
                        finish()
                    } else {
                        showToast("Failed to edit Ingredient")
                    }
                }
            } else{
                newQuantity.takeIf { it.isNullOrEmpty() }?.let { quantityET.error = "This field is required." }
                newMeasurement.takeIf { it.isNullOrEmpty() }?.let { measurementET.error = "This field is required." }
            }

        })
    }

    private fun updateIngredient(ingredientName: String, newQuantity: Float, newMeasurement: String, callback: (Boolean) -> Unit) {
        // Update the user's password in the database
        val sharedPrefs = getSharedPreferences("AppPrefs", MODE_PRIVATE)
        val currUser = sharedPrefs.getString("username","DEFAULT").toString()

        val document = usersRef.whereEqualTo("username", currUser).limit(1)

        document.get().addOnSuccessListener { documents ->
            for (document in documents) {
                val documentId = document.id
                val ingredientsArray = document.get("ingredient list") as? ArrayList<HashMap<String, Any>> ?: ArrayList()
                val outdatedIngredient = ingredientsArray.firstOrNull { it["ingredient"] == ingredientName }

                outdatedIngredient?.apply {
                    put("measurement",newMeasurement)
                    put("quantity",newQuantity)
                }

                // Update the document with the new map
                usersRef.document(documentId).update("ingredient list",ingredientsArray)
                    .addOnSuccessListener {
                        // Update successful
                        // Handle success, if needed
                        println("Array updated successfully")
                        showToast("${ingredientName} Ingredient Edited")
                        callback(true)
                    }
                    .addOnFailureListener { e ->
                        // Handle failure
                        // Log or display an error message
                        println("Error updating array: $e")
                        showToast("Failed to Edit Ingredient")
                        callback(false)
                    }
            }
        }
        .addOnFailureListener { e ->
            // Handle failure
            // Log or display an error message
        }
    }

    private fun deleteIngredient(ingredientName: String, callback: (Boolean) -> Unit) {
        val sharedPrefs = getSharedPreferences("AppPrefs", MODE_PRIVATE)
        val currUser = sharedPrefs.getString("username", "DEFAULT").toString()

        val document = usersRef.whereEqualTo("username", currUser).limit(1)

        document.get().addOnSuccessListener { documents ->
            for (document in documents) {
                val documentId = document.id
                val ingredientsArray =
                    document.get("ingredient list") as? ArrayList<HashMap<String, Any>> ?: emptyList()

                if(ingredientsArray.isNotEmpty()){
                    val newArray = ArrayList<HashMap<String, Any>>()

                    for (ingredient in ingredientsArray) {
                        val ingredientField = ingredient["ingredient"]
                        if (ingredientField != ingredientName) {
                            newArray.add(ingredient)
                        }
                    }

                    usersRef.document(documentId).update("ingredient list", newArray)
                        .addOnSuccessListener {
                            // Update successful
                            println("Array updated successfully")
                            showToast("${ingredientName} Ingredient Deleted")
                            callback(true)
                        }
                        .addOnFailureListener { e ->
                            // Handle error
                            println("Error updating array: $e")
                            showToast("Failed to delete Ingredient")
                            callback(false)
                        }
                }
            }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}