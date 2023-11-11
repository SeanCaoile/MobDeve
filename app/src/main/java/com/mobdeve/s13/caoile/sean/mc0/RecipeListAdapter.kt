package com.mobdeve.s13.caoile.sean.mc0

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class RecipeListAdapter(private val data: ArrayList<RecipeModel>, val recipeListClickListener: RecipeListClickListener): RecyclerView.Adapter<RecipeListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeListViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.dish_item, parent, false)

        return RecipeListViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecipeListViewHolder, position: Int) {
        holder.bindData(data.get(position))
        val recipe = data.get(position)

        holder.itemView.setOnClickListener {
            recipeListClickListener.onRecipeListItemClick(it, recipe, position)
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }
}