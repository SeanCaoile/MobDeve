package com.mobdeve.s13.caoile.sean.mc0

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class IngredientListAdapter (private val data: ArrayList<IngredientModel>, val ingredientsListListener: IngredientsListListener): RecyclerView.Adapter<IngredientsListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IngredientsListViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.ingredients_item_checkbox, parent, false)
        return IngredientsListViewHolder(view)
    }

    override fun onBindViewHolder(holder: IngredientsListViewHolder, position: Int) {
        holder.bindData(data.get(position))

        val ingredient = data.get(position)
        holder.itemView.setOnClickListener {
            ingredientsListListener.onIngredientsListItemClick(it, ingredient, position)
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }
}