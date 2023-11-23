package com.mobdeve.s13.caoile.sean.mc0

import android.content.ContentValues
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
                .whereEqualTo("username", user.toString())
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
                .whereEqualTo("username", user.toString())
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
                .whereEqualTo("name", recipeName)
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
        fun getFavoriteStrings(user: String, onResult: (ArrayList<String>) -> Unit) {
            val db = com.google.firebase.ktx.Firebase.firestore
            val favIDList: ArrayList<String> = arrayListOf()

            db.collection("users")
                .whereEqualTo("username", user)
                .get()
                .addOnSuccessListener { result ->
                    for (document in result) {
                        if (document != null) {
                            val dbFavStr = document.data?.get("favorites") as? ArrayList<DocumentReference>

                            dbFavStr?.forEach { fav ->
                                fav.get().addOnSuccessListener { favDocumentSnapshot ->
                                    if (favDocumentSnapshot.exists()) {
                                        val recipeId = fav.id
                                        favIDList.add(recipeId)
                                        Log.d("TAG", "Added recipe to favIDList: $recipeId")
                                    } else {
                                        Log.d("TAG", "Invalid recipe reference found: ${fav.id}")

                                        val userRef = document.reference
                                        userRef.update("favorites", FieldValue.arrayRemove(fav))
                                            .addOnSuccessListener {
                                                Log.d("TAG", "Removed invalid reference: ${fav.id}")
                                            }
                                            .addOnFailureListener { e ->
                                                Log.w("TAG", "Error removing invalid reference: ${e.message}")
                                            }
                                    }

                                    if (favIDList.size == dbFavStr.size) {
                                        onResult(favIDList)
                                    }
                                }.addOnFailureListener { e ->
                                    Log.w("TAG", "Error fetching favorite document: ${e.message}")
                                }
                            }
                        }
                    }
                }
                .addOnFailureListener { exception ->
                    Log.w("TAG", "Error getting documents.", exception)
                }
        }

        fun getFavorites(currentUser: String, onResult: (ArrayList<RecipeModel>) -> (Unit)) : ArrayList<RecipeModel>
        {
            val firestore = Firebase.firestore
            val recipes = ArrayList<RecipeModel>()

            this.getFavoriteStrings(currentUser) {
                val recipesDb = firestore.collection("recipes")

                var favStrings = it

                recipesDb.get().addOnSuccessListener { result ->
                    for (document in result) {
                        if(favStrings.contains(document.id)) {
                            var creator = document.getString("creator").toString()
                            if (creator != null && (creator == currentUser || creator == "The Guru")){
                                val ingredients = ArrayList<IngredientModel>()
                                val ingredientElement = document.get("ingredients") as List<Map<String, Any>>

                                for (map in ingredientElement) {
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