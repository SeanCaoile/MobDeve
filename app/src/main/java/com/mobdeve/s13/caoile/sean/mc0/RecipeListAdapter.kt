package com.mobdeve.s13.caoile.sean.mc0

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class RecipeListAdapter(private val data: ArrayList<RecipeModel>, val recipeListClickListener: RecipeListClickListener): RecyclerView.Adapter<RecipeListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeListViewHolder {
        // Create a LayoutInflater using the parent's (i.e. RecyclerView's) context
        val inflater = LayoutInflater.from(parent.context)
        // Inflate a new View given the item_layout.xml item view we created.
        val view = inflater.inflate(R.layout.dish_item, parent, false)

        val recipeListViewHolder = RecipeListViewHolder(view)



        recipeListViewHolder.itemView.setOnClickListener {

        }

        // Return a new instance of our MyViewHolder passing the View object we created
        return RecipeListViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecipeListViewHolder, position: Int) {
        // Please note that bindData is a function we created to adhere to encapsulation. There are
        // many ways to implement the binding of data.
        holder.bindData(data.get(position))
        val recipe = data.get(position)

        holder.itemView.setOnClickListener {
            recipeListClickListener.onRecipeListItemClick(it, recipe)
        }
    }

    override fun getItemCount(): Int {
        // This needs to be modified, so don't forget to add this in.
        return data.size
    }
}