package com.mobdeve.s13.caoile.sean.mc0

import android.view.View
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class RecipeIngredientsViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
    private val checkbox: CheckBox = itemView.findViewById(R.id.ingredientCheckbox)
    private val itemTv: TextView = itemView.findViewById(R.id.item_ingredient)
    private val item_quantity: TextView = itemView.findViewById(R.id.item_quantity)
    private val quantityType: TextView = itemView.findViewById(R.id.quantityType)

    fun bindData(ingredient: IngredientModel) {
        checkbox.isChecked = ingredient.isChecked
        itemTv.text = ingredient.ingredient
        item_quantity.text = ingredient.quantity.toString()
        quantityType.text = ingredient.measurement

        checkbox.setOnCheckedChangeListener { _, isChecked ->
            ingredient.isChecked = isChecked
        }
    }
}