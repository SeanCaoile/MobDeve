package com.mobdeve.s13.caoile.sean.mc0

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class RecipeIngredientsViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
    private val itemTv: TextView = itemView.findViewById(R.id.item_ingredient)
    private val item_quantity: TextView = itemView.findViewById(R.id.item_quantity)
    private val quantityType: TextView = itemView.findViewById(R.id.quantityType)

    fun bindData(ingredient: IngredientModel) {
        itemTv.text = ingredient.ingredient
        item_quantity.text = ingredient.quantity.toString()
        quantityType.text = ingredient.measurement
    }
}