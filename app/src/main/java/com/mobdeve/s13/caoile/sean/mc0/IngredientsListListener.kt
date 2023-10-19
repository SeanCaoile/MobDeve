package com.mobdeve.s13.caoile.sean.mc0

import android.view.View

interface IngredientsListListener {
    fun onIngredientsListItemClick(view: View, ingredient: IngredientModel, position: Int)
}