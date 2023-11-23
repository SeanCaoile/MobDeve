package com.mobdeve.s13.caoile.sean.mc0

import android.content.ContentValues
import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.firestore

class DataGenerator {
    companion object{
        fun generateRecipes(currentUser: String, onResult: (ArrayList<RecipeModel>) -> (Unit)) : ArrayList<RecipeModel>
        {
            val firestore = Firebase.firestore
            val recipes = ArrayList<RecipeModel>()

            val recipesDb = firestore.collection("recipes")

            recipesDb.get().addOnSuccessListener { result ->
                for (document in result) {
                    var creator = document.getString("creator").toString()
                    if (creator != null && (creator == currentUser || creator == "The Guru")){
                        val ingredients = ArrayList<IngredientModel>()
                        val ingredientElement = document.get("ingredients") as List<Map<String, Any>>

                        for (map in ingredientElement) {
                            val ingredientName = map["ingredient"].toString()
                            val measurement = map["measurement"].toString()
                            val quantity: Float = (map["quantity"] as? Number)?.toFloat() ?: 0.0f
                            val ingredient = IngredientModel(ingredientName, quantity, measurement)

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
                onResult(recipes)
            }
            return recipes
        }

        fun searchRecipe(searchRecipe: String,currentUser: String, onResult: (ArrayList<RecipeModel>) -> (Unit)) : ArrayList<RecipeModel>
        {
            val firestore = Firebase.firestore
            val recipes = ArrayList<RecipeModel>()

            val recipesDb = firestore.collection("recipes")

            recipesDb.get().addOnSuccessListener { result ->
                for (document in result) {
                    var creator = document.getString("creator").toString()
                    if (creator != null && (creator == currentUser || creator == "The Guru")){
                        val ingredients = ArrayList<IngredientModel>()
                        val ingredientElement = document.get("ingredients") as List<Map<String, Any>>

                        val name = document.getString("name").toString()
                        Log.d("STRING CHECK",name)
                        Log.d("STRING CHECK",searchRecipe)

                        if (name.contains(searchRecipe, ignoreCase = true)){

                            for (map in ingredientElement) {

                                val ingredientName = map["ingredient"].toString()
                                val measurement = map["measurement"].toString()
                                val quantity: Float = (map["quantity"] as? Number)?.toFloat() ?: 0.0f
                                val ingredient = IngredientModel(ingredientName, quantity, measurement)

                                ingredients.add(ingredient)
                            }
                            var addon = "by: "
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
            return recipes
        }

        fun generateUsers(currentUser: String, onResult: (ArrayList<UserModel>) -> (Unit)) : ArrayList<UserModel>
        {
            val firestore = Firebase.firestore
            val users = ArrayList<UserModel>()
            firestore.collection("users")
                .get()
                .addOnSuccessListener { result ->
                    for (document in result) {
                        val username = document.getString("username")
                        var favList: ArrayList<DocumentReference> = arrayListOf<DocumentReference>()
                        try{
                            favList = document.get("favorites") as ArrayList<DocumentReference>
                        }
                        catch (e: Exception) {
                            Log.d("Error TAG" , e.toString())
                        }

                        if (username != null && currentUser != username){
                            if(favList.isEmpty()) {
                                Log.d("TAGGERS", username.toString() + " favlist is empty")
                                val user = UserModel(username, arrayListOf<RecipeModel>())
                                users.add(user)
                                onResult(users)
                            }
                            else {
                                Log.d("TAGGERS", username.toString() + " favlist is not empty")
                                DBDataGetter.getFavorites(username) {

                                    val updatedRecipes = it
                                    Log.d("TAG", it.toString())
                                    Log.d("TAG", "DONE GETTING FAVS FOR OTHER USER " + username)

                                    val user = UserModel(username, updatedRecipes)

                                    users.add(user)
                                    onResult(users)
                                }
                            }
                            try{
                                Log.d("USER TAG", "getting " + username.toString())
                            }
                            catch(e: Exception) {
                                Log.d("AWOOOOGA", "Catch")
                            }
                        }
                    }
                } .addOnFailureListener { exception ->
                    Log.w(ContentValues.TAG, "Error getting documents",exception)
                }

            return users
        }

        fun searchUser(currentUser: String, searchUser: String, onResult: (ArrayList<UserModel>) -> (Unit)) : ArrayList<UserModel>
        {
            val firestore = Firebase.firestore
            val users = ArrayList<UserModel>()
            firestore.collection("users")
                .whereGreaterThanOrEqualTo("username", searchUser)
                .whereLessThanOrEqualTo("username", searchUser + "\uf8ff")
                .get()
                .addOnSuccessListener { result ->
                    for (document in result) {
                        val username = document.getString("username")
                        var favList: ArrayList<DocumentReference> = arrayListOf<DocumentReference>()
                        try{
                            favList = document.get("favorites") as ArrayList<DocumentReference>
                        }
                        catch (e: Exception) {
                            Log.d("Error TAG" , e.toString())
                        }

                        if (username != null && currentUser != username){
                            if(favList.isEmpty()) {
                                Log.d("TAGGERS", username.toString() + " favlist is empty")
                                val user = UserModel(username, arrayListOf<RecipeModel>())
                                users.add(user)
                                onResult(users)
                            }
                            else {
                                Log.d("TAGGERS", username.toString() + " favlist is not empty")
                                DBDataGetter.getFavorites(username) {

                                    val updatedRecipes = it
                                    Log.d("TAG", "DONE GETTING FAVS FOR OTHER USER in Search" + username)

                                    val user = UserModel(username, updatedRecipes)
                                    users.add(user)
                                    onResult(users)
                                }
                            }
                        }
                    }
                } .addOnFailureListener { exception ->
                    Log.w(ContentValues.TAG, "Error getting documents",exception)
                }

            return users
        }
    }
}