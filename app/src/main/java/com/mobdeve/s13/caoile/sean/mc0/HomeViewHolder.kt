package com.mobdeve.s13.caoile.sean.mc0

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
class HomeViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

    private val favDishTV : TextView = itemView.findViewById(R.id.dishName)
    private val favDishIV : ImageView = itemView.findViewById(R.id.dishImg)
    fun bindData(favDish: RecipeModel){
        favDishTV.text = favDish.recipeName
//        favDishIV.setImageResource(favDish.imageURL)
    }
}