package com.mobdeve.s13.caoile.sean.mc0;

import java.io.Serializable

class RecipeModel(ingredients: ArrayList<IngredientModel>, recipeName: String, instructions: String, creator: String, imageId : Int) : Serializable {
    var ingredients = ingredients
        private set

    var recipeName = recipeName
        private set

    var instructions = instructions
        private set

    var creator = creator
        private set

    var imageId = imageId
        private set

    override fun toString(): String {
        return "Recipe{" +
                "ingredients='" + ingredients + '\'' +
                ", name='" + recipeName +
                ", instructions='" + instructions +
                ", creator='" + creator +
                ", image ='" + imageId +
                '}'
    }
}
