package com.mobdeve.s13.caoile.sean.mc0

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView

class IngredientListAdapter (private val data: ArrayList<IngredientModel>, val ingredientsListListener: IngredientsListListener): RecyclerView.Adapter<IngredientsListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IngredientsListViewHolder {
        // Create a LayoutInflater using the parent's (i.e. RecyclerView's) context
        val inflater = LayoutInflater.from(parent.context)
        // Inflate a new View given the item_layout.xml item view we created.
        val view = inflater.inflate(R.layout.ingredients_item, parent, false)
        // Return a new instance of our MyViewHolder passing the View object we created
        return IngredientsListViewHolder(view)
    }

    override fun onBindViewHolder(holder: IngredientsListViewHolder, position: Int) {
        // Please note that bindData is a function we created to adhere to encapsulation. There are
        // many ways to implement the binding of data.
        holder.bindData(data.get(position))

        val ingredient = data.get(position)


        holder.itemView.setOnClickListener {
            ingredientsListListener.onIngredientsListItemClick(it, ingredient, position)
        }
    }

    override fun getItemCount(): Int {
        // This needs to be modified, so don't forget to add this in.
        return data.size
    }
}