package com.mobdeve.s13.caoile.sean.mc0

import android.content.ContentValues
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.ktx.firestore

class DBDataGetter {
    companion object{


        fun getFavoriteStrings(user:String, onResult: (ArrayList<String>) -> (Unit)) : ArrayList<String>
        {


            val db = com.google.firebase.ktx.Firebase.firestore
            var favIDList: ArrayList<String> = arrayListOf<String>()

            Log.i(ContentValues.TAG, "STARTING DB CONTENT CHECK FOR FAVORITES IN USER")
            db.collection("users")
                .whereEqualTo("username", user.toString()) //set to username later
                .get()
                .addOnSuccessListener { result ->
                    for (document in result) {
                        val dbFavStr = result.documents[0].data?.get("favorites") as ArrayList<String>

                        for(fav in dbFavStr) {

                            Log.d("TAG", fav.toString())
                            val recipe: String = fav.toString()
                            favIDList.add(recipe)
                            Log.d("TAG", "Arraylist is now")
                            Log.d("TAG", favIDList.toString())

                        }


                    }
                    onResult(favIDList)
                }
                .addOnFailureListener { exception ->
                    Log.w(ContentValues.TAG, "Error getting documents.", exception)

                }
            return favIDList

        }



        fun getIngredients(user:String, onResult: (ArrayList<IngredientModel>) -> (Unit)) : ArrayList<IngredientModel>
        {


            val db = com.google.firebase.ktx.Firebase.firestore
            var ingredientsList: ArrayList<IngredientModel> = arrayListOf<IngredientModel>()

            Log.i(ContentValues.TAG, "STARTING DB CONTENT CHECK FOR USER INGREDIENTS")
            db.collection("users")
                .whereEqualTo("username", user.toString()) //set to username later
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