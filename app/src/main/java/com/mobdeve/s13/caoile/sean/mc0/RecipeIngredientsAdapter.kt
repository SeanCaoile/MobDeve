package com.mobdeve.s13.caoile.sean.mc0

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class RecipeIngredientsAdapter(private val withCheckBox: Boolean, private val data: ArrayList<IngredientModel>): RecyclerView.Adapter<RecipeIngredientsViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeIngredientsViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        val layoutResourceId = if (withCheckBox) {
            R.layout.ingredients_item_checkbox
        } else {
            R.layout.ingredients_item_no_checkbox
        }
        val view = inflater.inflate(layoutResourceId, parent, false)

        return RecipeIngredientsViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecipeIngredientsViewHolder, position: Int) {
        holder.bindData(data.get(position))
    }

    override fun getItemCount(): Int {
        return data.size
    }
}