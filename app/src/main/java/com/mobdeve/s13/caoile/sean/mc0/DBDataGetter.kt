package com.mobdeve.s13.caoile.sean.mc0

import android.content.ContentValues
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.ktx.firestore

class DBDataGetter {
    companion object{
        fun addFavoriteReference(reference: DocumentReference, user: String, favorited: Boolean) {
            val db = com.google.firebase.ktx.Firebase.firestore

            db.collection("users")
                .whereEqualTo("username", user.toString()) //set to username later
                .get()
                .addOnSuccessListener { result ->
                    for (document in result) {
                        if(document != null) {
                            var userRef: DocumentReference = document.reference
                            if(favorited == false) {
                                userRef.update("favorites", FieldValue.arrayUnion(reference))
                                    .addOnSuccessListener {
                                        Log.d("TAG", "Item added to array successfully!")
                                    }
                                    .addOnFailureListener { e ->
                                        Log.w("TAG", "Error adding item to array", e)
                                    }
                            }
                            else {
                                userRef.update("favorites", FieldValue.arrayRemove(reference))
                                    .addOnSuccessListener {
                                        Log.d("TAG", "Reference removed successfully!")
                                    }
                                    .addOnFailureListener { e ->
                                        Log.w("TAG", "Error removing reference", e)
                                    }
                            }

                        }
                    }
                }
                .addOnFailureListener { exception ->
                    Log.w(ContentValues.TAG, "Error getting documents.", exception)
                }


        }

        fun checkIfFavorited(recipeRef: DocumentReference, user: String, onResult: (Boolean) -> (Unit)) {
            val db = com.google.firebase.ktx.Firebase.firestore
            Log.i(ContentValues.TAG, "STARTING DB CONTENT CHECK FOR FAVORITE STATUS")
            db.collection("users")
                .whereEqualTo("username", user.toString()) //set to username later
                .get()
                .addOnSuccessListener { result ->
                    for (document in result) {
                        if(document != null) {
                            val dbFavStr = result.documents[0].data?.get("favorites") as ArrayList<DocumentReference>
                            if(dbFavStr.contains(recipeRef)) {
                                onResult(true)
                            }
                            else {
                                onResult(false)
                            }
                        }
                    }
                }
                .addOnFailureListener { exception ->
                    Log.w(ContentValues.TAG, "Error getting documents.", exception)
                }
        }

        fun getCurrentRecipeReference(recipeName: String, creator: String, onResult: (DocumentReference) -> (Unit)) {
            val db = com.google.firebase.ktx.Firebase.firestore
            Log.d("TAG", "finding current reference in DB " + creator + " " + recipeName)
            db.collection("recipes")
                .whereEqualTo("name", recipeName) //set to username later
                //.whereEqualTo("creator" , creator)
                .get()
                .addOnSuccessListener { result ->
                    Log.d("TAG" , "Looking at result " + result.toString())
                    for (document in result) {
                        Log.d("TAG" , "Looking at result " + document.toString())
                        Log.d("T", "Comparing " + document.get("creator").toString() + " with " + creator)
                        if(document.get("creator").toString() == creator)
                            {
                                Log.d("TAG" , "Found reference")
                                val recipeReference = document.reference
                                onResult(recipeReference)
                            }

                    }
                }
                .addOnFailureListener { exception ->
                    Log.w(ContentValues.TAG, "Error getting documents.", exception)
                }
        }
        fun getFavoriteStrings(user:String, onResult: (ArrayList<String>) -> (Unit)) : ArrayList<String>
        {
            val db = com.google.firebase.ktx.Firebase.firestore
            var favIDList: ArrayList<String> = arrayListOf<String>()

//            Log.i(ContentValues.TAG, "STARTING DB CONTENT CHECK FOR FAVORITES IN USER")
            db.collection("users")
                .whereEqualTo("username", user.toString()) //set to username later
                .get()
                .addOnSuccessListener { result ->
                    for (document in result) {
                        if(document != null) {
                            val dbFavStr = result.documents[0].data?.get("favorites") as ArrayList<DocumentReference>

                            for(fav in dbFavStr) {

                                Log.d("TAG", fav.id)
                                val recipe: String = fav.id
                                favIDList.add(recipe)
                                Log.d("TAG", "Arraylist is now")
                                Log.d("TAG", favIDList.toString())

                            }
                        }



                    }
                    onResult(favIDList)
                }
                .addOnFailureListener { exception ->
                    Log.w(ContentValues.TAG, "Error getting documents.", exception)
                }
            return favIDList
        }

        fun getFavorites(currentUser: String, onResult: (ArrayList<RecipeModel>) -> (Unit)) : ArrayList<RecipeModel>
        {
            val firestore = Firebase.firestore
            val recipes = ArrayList<RecipeModel>()

            this.getFavoriteStrings(currentUser) {
                val recipesDb = firestore.collection("recipes")

                var favStrings = it
//                Log.d("TAG", "Printing favStrings in getFavorites")
//                Log.d("TAG", favStrings.toString())
                recipesDb.get().addOnSuccessListener { result ->
                    for (document in result) {
//                        Log.d("TAG", "Printing current recipe ID")
//                        Log.d("TAG", document.id)
                        if(favStrings.contains(document.id)) {
//                            Log.d("TAG", "Favorite Recipe Found " + document.id)

                            var creator = document.getString("creator").toString()
                            if (creator != null && (creator == currentUser || creator == "The Guru")){
                                val ingredients = ArrayList<IngredientModel>()
                                val ingredientElement = document.get("ingredients") as List<Map<String, Any>>

                                //get each ingredient
                                for (map in ingredientElement) {
                                    // Now you can access individual elements in the map
                                    val ingredientName = map["ingredient"].toString()
                                    val measurement = map["measurement"].toString()
                                    val quantity: Float = (map["quantity"] as? Number)?.toFloat() ?: 0.0f
                                    val ingredient: IngredientModel = IngredientModel(ingredientName, quantity, measurement)

                                    ingredients.add(ingredient)
                                }
                                var addon = "by: "
                                val name = document.getString("name").toString()
                                creator = addon.plus(creator)
                                val instructions = document.getString("instructions").toString()
                                val image = document.getString("imageURI").toString()

                                val recipe = RecipeModel(ingredients,name,instructions,creator,image)

                                recipes.add(recipe)
//                                Log.d("TAG", "Added Rec list is now")
//                                Log.d("TAG", recipe.toString())
                            }
                        }
                    }
                    onResult(recipes)
                }
            }
            return recipes
        }

        fun getIngredients(user:String, onResult: (ArrayList<IngredientModel>) -> (Unit))
        {
            val db = com.google.firebase.ktx.Firebase.firestore
            var ingredientsList: ArrayList<IngredientModel> = arrayListOf<IngredientModel>()

//            Log.i(ContentValues.TAG, "STARTING DB CONTENT CHECK FOR USER INGREDIENTS")
            db.collection("users")
                .whereEqualTo("username", user)
                .get()
                .addOnSuccessListener { result ->
                    for (document in result) {
                        val dbIngredients = result.documents.getOrNull(0)?.data?.get("ingredient list") != null
                        if (dbIngredients) {
                            val dbIngredients = result.documents[0].data?.get("ingredient list") as ArrayList<Map<String, Any>>

                            for(userIngredient in dbIngredients) {
                                val name: String = userIngredient["ingredient"].toString()
                                val measurement = userIngredient["measurement"].toString()
                                val quantity = userIngredient["quantity"].toString()
                                val newIngredient : IngredientModel = IngredientModel(name, quantity.toFloat(), measurement)
//                                Log.d("NEWINGREDIENT","$newIngredient")
                                ingredientsList.add(newIngredient)
                            }
                        }
                    }
                    onResult(ingredientsList)
                }
                .addOnFailureListener { exception ->
                    Log.w(ContentValues.TAG, "Error getting documents.", exception)
                }
        }
    }
}