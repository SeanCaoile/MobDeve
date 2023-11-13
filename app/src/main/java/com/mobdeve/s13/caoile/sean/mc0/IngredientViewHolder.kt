package com.mobdeve.s13.caoile.sean.mc0

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class IngredientViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val tvIngredientName: TextView = view.findViewById(R.id.ingredientName)
    val tvQuantity: TextView = view.findViewById(R.id.quantity)
    val tvMeasurement: TextView = view.findViewById(R.id.measurement)

    fun bindData(ingredient: IngredientModel) {
        tvIngredientName.text = ingredient.ingredient
        tvQuantity.text = ingredient.quantity.toString()
        tvMeasurement.text = ingredient.measurement
    }
}
