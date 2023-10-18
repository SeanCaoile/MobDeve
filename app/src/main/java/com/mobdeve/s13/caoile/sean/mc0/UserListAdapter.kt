package com.mobdeve.s13.caoile.sean.mc0

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView

class UserListAdapter(private val data: ArrayList<UserModel>, val userListClickListener: UserListClickListener): RecyclerView.Adapter<UserListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserListViewHolder {
        // Create a LayoutInflater using the parent's (i.e. RecyclerView's) context
        val inflater = LayoutInflater.from(parent.context)
        // Inflate a new View given the item_layout.xml item view we created.
        val view = inflater.inflate(R.layout.user_item, parent, false)
        // Return a new instance of our MyViewHolder passing the View object we created
        return UserListViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserListViewHolder, position: Int) {
        // Please note that bindData is a function we created to adhere to encapsulation. There are
        // many ways to implement the binding of data.
        holder.bindData(data.get(position))

        val user = data.get(position)
        holder.setDeleteOnClickListener(View.OnClickListener {
            // Inform the user of the deleted element
            Toast.makeText(
                holder.itemView.context,
                "${data[position].username} has been unfollowed.",
                Toast.LENGTH_SHORT
            ).show()

            // Remove the element from the data (i.e. ArrayList)
            data.removeAt(position)
            notifyItemRemoved(position)
            notifyItemRangeChanged(position, itemCount)
        })

        holder.itemView.setOnClickListener {
            userListClickListener.onUserListItemClick(it, user, position)
        }
    }

    override fun getItemCount(): Int {
        // This needs to be modified, so don't forget to add this in.
        return data.size
    }
}