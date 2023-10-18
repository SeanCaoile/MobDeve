package com.mobdeve.s13.caoile.sean.mc0

import android.content.Intent
import android.os.Bundle
import android.provider.ContactsContract.Data
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.text.FieldPosition

class UsersFragment : Fragment(), UserListClickListener {
    lateinit var listener: UserListClickListener
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        listener = this
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_users, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //getting users
        val users = DataGenerator.generateUsers()
        Log.d("TAG", "Generating Users")
        Log.d("TAG", users.get(0).toString())
        Log.d("TAG", users.get(1).toString())
        //assign users to ItemAdapter
        val itemAdapter = UserListAdapter(users, listener)

        val recyclerView: RecyclerView = view.findViewById(R.id.userList)
        recyclerView.layoutManager = LinearLayoutManager(context)

        recyclerView.adapter = itemAdapter
    }

    override fun onUserListItemClick(view: View, user: UserModel, position: Int) {
        val intent = Intent(activity, UserActivity::class.java)

        intent.putExtra(UserActivity.USERNAME_KEY, user.username)
        intent.putExtra(UserActivity.FAVDISH_KEY, user.favDishes)
        startActivity(intent)
    }
}