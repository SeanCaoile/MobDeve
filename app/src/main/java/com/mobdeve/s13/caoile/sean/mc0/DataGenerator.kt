package com.mobdeve.s13.caoile.sean.mc0

import android.content.ContentValues
import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.auth.User
import com.google.firebase.firestore.firestore

class DataGenerator {
    companion object{
        private val ingredient1 : IngredientModel = IngredientModel("Egg", "1".toFloat(), "")
        private val ingredient2 : IngredientModel = IngredientModel("Salt", "100".toFloat(), "mg")
        private val ingredient3 : IngredientModel = IngredientModel("Butter", "3".toFloat(), "tbsp")
        private val ingredient4 : IngredientModel = IngredientModel("Pepper", "150".toFloat(), "mg")
        private val recipe1 : RecipeModel = RecipeModel(
            arrayListOf<IngredientModel>(ingredient1, ingredient2, ingredient3),
            "Scrambled Eggs",
            "1. Whisk eggs, and salt in small bowl. Melt butter in non-stick skillet over medium heat.\n" +
                    "2. Pour in egg mixture and reduce heat to medium-low. As eggs begin to set, " +
                    "gently move spatula across bottom and side of skillet to form large, soft curds.\n" +
                    "3. Cook until eggs are thickened and no visible liquid egg remains, but the eggs are not dry.",
            "The Cook",
//            R.drawable.scrambledegg
            "https://firebasestorage.googleapis.com/v0/b/mobdeve-s13--seancaoile.appspot.com/o/scrambledegg.png?alt=media&token=9280b7f7-eb0d-4570-93de-e51184cbcd12&_gl=1*8i2jt8*_ga*NTUxNzEzNDI5LjE2OTgwNjIzMzY.*_ga_CW55HF8NVT*MTY5OTMzOTYwNC4xNi4xLjE2OTkzNDE5OTAuNDIuMC4w"
        )
        private val recipe2 : RecipeModel = RecipeModel(
            arrayListOf<IngredientModel>(ingredient1, ingredient2, ingredient3),
            "Pizza",
            "1. Knead and flour Dough . \n" +
                    "2. Spread tomato paste around flattened dough. Add Cheese and toppings accordingly . \n" +
                    "3. Bake pizza in oven for 10-20 minutes ",
            "The Cook",
//            R.drawable.pizzafritta
            "https://firebasestorage.googleapis.com/v0/b/mobdeve-s13--seancaoile.appspot.com/o/scrambledegg.png?alt=media&token=9280b7f7-eb0d-4570-93de-e51184cbcd12&_gl=1*8i2jt8*_ga*NTUxNzEzNDI5LjE2OTgwNjIzMzY.*_ga_CW55HF8NVT*MTY5OTMzOTYwNC4xNi4xLjE2OTkzNDE5OTAuNDIuMC4w"
        )

        private val user1: UserModel = UserModel("John", arrayListOf<RecipeModel>(recipe1))
        private val user2: UserModel = UserModel("Mary", arrayListOf<RecipeModel>(recipe1))
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
                        //search function
                        if (name.contains(searchRecipe, ignoreCase = true)){
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
//                            val recipes = document.get("favorites") as ArrayList<RecipeModel>

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

                        if (username != null && currentUser != username){
//                            val recipes = document.get("favorites") as ArrayList<RecipeModel>
                            val user = UserModel(username, arrayListOf<RecipeModel>(
                                recipe1))

                            users.add(user)
                        }
                    }
                    Log.d("new searches","$users")
                    onResult(users)
                } .addOnFailureListener { exception ->
                    Log.w(ContentValues.TAG, "Error getting documents",exception)
                }

            return users
        }

        fun generateFavDishes() : ArrayList<RecipeModel>
        {
            val dishes = ArrayList<RecipeModel>()
            dishes.add(recipe1)
            dishes.add(recipe2)

            return dishes
        }

        fun generateIngredients() : ArrayList<IngredientModel>
        {
            val ingredients = ArrayList<IngredientModel>()
//            ingredients.add(ingredient1)
//            ingredients.add(ingredient3)
//            ingredients.add(ingredient4)
            return ingredients
        }
    }
}