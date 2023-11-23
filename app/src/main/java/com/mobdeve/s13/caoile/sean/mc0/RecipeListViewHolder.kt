package com.mobdeve.s13.caoile.sean.mc0

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class RecipeListViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
    private val imageIV: ImageView = itemView.findViewById(R.id.dishImg)
    private val nameTv: TextView = itemView.findViewById(R.id.nameTv)

    fun bindData(recipe: RecipeModel) {
        Glide.with(itemView.context)
            .load(recipe.imageURL)
            .into(imageIV)

        nameTv.text = recipe.recipeName
    }
}