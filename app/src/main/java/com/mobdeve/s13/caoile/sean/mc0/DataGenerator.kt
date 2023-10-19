package com.mobdeve.s13.caoile.sean.mc0

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
            R.drawable.scrambledegg
        )
        private val recipe2 : RecipeModel = RecipeModel(
            arrayListOf<IngredientModel>(ingredient1, ingredient2, ingredient3),
            "Pizza",
            "1. Knead and flour Dough . \n" +
                    "2. Spread tomato paste around flattened dough. Add Cheese and toppings accordingly . \n" +
                    "3. Bake pizza in oven for 10-20 minutes ",
            "The Cook",
            R.drawable.pizzafritta
        )

        private val user1: UserModel = UserModel("John", arrayListOf<RecipeModel>(recipe1))
        private val user2: UserModel = UserModel("Mary", arrayListOf<RecipeModel>(recipe1))
        fun generateRecipes() : ArrayList<RecipeModel>
        {
            return arrayListOf<RecipeModel>(recipe1)
        }

        fun generateUsers() : ArrayList<UserModel>
        {
            val users = ArrayList<UserModel>()
            users.add(user1)
            users.add(user2)

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
            ingredients.add(ingredient1)
            ingredients.add(ingredient3)
            ingredients.add(ingredient4)
            return ingredients
        }

    }
}