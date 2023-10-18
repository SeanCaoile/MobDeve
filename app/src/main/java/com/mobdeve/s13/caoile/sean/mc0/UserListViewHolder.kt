package com.mobdeve.s13.caoile.sean.mc0

import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class UserListViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
    private val userTV : TextView = itemView.findViewById(R.id.username)

    private val followButton : ImageButton = itemView.findViewById(R.id.followButton)
    fun bindData(user: UserModel){
        userTV.text = user.username
    }

    fun setDeleteOnClickListener(onClickListener: View.OnClickListener) {
        this.followButton.setOnClickListener(onClickListener)
    }
}