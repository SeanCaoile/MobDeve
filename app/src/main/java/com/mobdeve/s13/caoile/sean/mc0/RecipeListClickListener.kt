package com.mobdeve.s13.caoile.sean.mc0

import android.view.View

interface RecipeListClickListener {

    fun onRecipeListItemClick(view: View, recipe: RecipeModel, position: Int)
}