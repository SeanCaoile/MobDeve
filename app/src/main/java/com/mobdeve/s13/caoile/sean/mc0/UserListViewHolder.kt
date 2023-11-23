package com.mobdeve.s13.caoile.sean.mc0

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class UserListViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
    private val userTV : TextView = itemView.findViewById(R.id.username)

    fun bindData(user: UserModel){
        userTV.text = user.username
    }
}