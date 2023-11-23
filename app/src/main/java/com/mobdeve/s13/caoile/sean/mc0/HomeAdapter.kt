package com.mobdeve.s13.caoile.sean.mc0

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
class HomeAdapter(private val favdishList: List<RecipeModel>) : RecyclerView.Adapter<HomeViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.favdish_item, parent, false)

        return HomeViewHolder(view)
    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        holder.bindData(favdishList.get(position))
    }

    override fun getItemCount(): Int {
        return favdishList.size
    }
}
