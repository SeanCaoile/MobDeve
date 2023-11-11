package com.mobdeve.s13.caoile.sean.mc0

import android.content.ContentValues
import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.ktx.firestore

class DBDataGetter {
    companion object{



        fun getIngredients(onResult: (ArrayList<IngredientModel>) -> (Unit)) : ArrayList<IngredientModel>
        {
            val db = com.google.firebase.ktx.Firebase.firestore
            var ingredientsList: ArrayList<IngredientModel> = arrayListOf<IngredientModel>()

            Log.i(ContentValues.TAG, "STARTING DB CONTENT CHECK FOR USER INGREDIENTS")
            db.collection("users")
                .whereEqualTo("username", "Bob") //set to username later
                .get()
                .addOnSuccessListener { result ->
                    for (document in result) {
                        val dbIngredients = result.documents[0].data?.get("ingredient list") as ArrayList<Map<String, Any>>

                        for(userIngredient in dbIngredients) {

                            Log.d("TAG", userIngredient.toString())
                            val name: String = userIngredient["ingredient"].toString()
                            val measurement = userIngredient["measurement"].toString()
                            val quantity = userIngredient["quantity"].toString()
                            val newIngredient : IngredientModel = IngredientModel(name, quantity.toFloat(), measurement)
                            ingredientsList.add(newIngredient)
                            Log.d("TAG", "Arraylist is now")
                            Log.d("TAG", ingredientsList.toString())
                            onResult(ingredientsList)
                        }


                    }
                }
                .addOnFailureListener { exception ->
                    Log.w(ContentValues.TAG, "Error getting documents.", exception)

                }
            return ingredientsList

        }

    }
}