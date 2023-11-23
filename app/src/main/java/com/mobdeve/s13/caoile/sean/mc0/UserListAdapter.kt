package com.mobdeve.s13.caoile.sean.mc0

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class UserListAdapter(private val data: ArrayList<UserModel>, val userListClickListener: UserListClickListener): RecyclerView.Adapter<UserListViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserListViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.user_item, parent, false)

        return UserListViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserListViewHolder, position: Int) {
        holder.bindData(data.get(position))
        val user = data.get(position)

        holder.itemView.setOnClickListener {
            userListClickListener.onUserListItemClick(it, user, position)
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }
}