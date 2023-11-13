package com.mobdeve.s13.caoile.sean.mc0

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class IngredientsAdapter(private val data: ArrayList<IngredientModel>): RecyclerView.Adapter<IngredientViewHolder>() {

    private val ingredientsList = mutableListOf<String>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IngredientViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.add_ingredient_item, parent, false)
        return IngredientViewHolder(view)
    }

    override fun onBindViewHolder(holder: IngredientViewHolder, position: Int) {
        val ingredient = ingredientsList[position]
        holder.tvIngredientName.text = ingredient
        holder.tvQuantity
    }

    override fun getItemCount(): Int {
        return ingredientsList.size
    }

    fun addIngredient(ingredient: String) {
        ingredientsList.add(ingredient)
        notifyDataSetChanged()
    }

    fun getIngredients(): List<String> {
        return ingredientsList
    }
}
