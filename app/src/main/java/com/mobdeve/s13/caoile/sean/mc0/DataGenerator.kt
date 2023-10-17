package com.mobdeve.s13.caoile.sean.mc0

class DataGenerator {
    companion object{
        private val ingredient1 : IngredientModel = IngredientModel("Egg", "1".toFloat(), "")
        private val ingredient2 : IngredientModel = IngredientModel("Salt", "100".toFloat(), "mg")
        private val ingredient3 : IngredientModel = IngredientModel("Butter", "3".toFloat(), "tbsp")
        private val recipe1 : RecipeModel = RecipeModel(
            arrayListOf<IngredientModel>(ingredient1, ingredient2, ingredient3),
            "Scrambled Eggs",
            "1. Whisk eggs, and salt in small bowl. Melt butter in non-stick skillet over medium heat.\n" +
                    "2. Pour in egg mixture and reduce heat to medium-low. As eggs begin to set, " +
                    "gently move spatula across bottom and side of skillet to form large, soft curds.\n" +
                    "3. Cook until eggs are thickened and no visible liquid egg remains, but the eggs are not dry.",
            "The Cook",
            R.drawable.ScrambledEgg
        )

        fun generateData() : ArrayList<RecipeModel>
        {
            return arrayListOf<RecipeModel>(recipe1)
        }

    }
}