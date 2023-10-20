package com.mobdeve.s13.caoile.sean.mc0

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class IngredientsListViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
    private val ingredientTv : TextView = itemView.findViewById(R.id.item_ingredient)
    private val quantityTv : TextView = itemView.findViewById(R.id.item_quantity)
    private val typeTv : TextView = itemView.findViewById(R.id.quantityType)

    fun bindData(ingredient: IngredientModel) {
        ingredientTv.text = ingredient.name
        quantityTv.text = ingredient.quantity.toString()
        typeTv.text = ingredient.quantityType
    }
}