package com.mobdeve.s13.caoile.sean.mc0

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore

class UsersFragment : Fragment(), UserListClickListener {
    lateinit var listener: UserListClickListener
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        listener = this
        return inflater.inflate(R.layout.fragment_users, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sharedPrefs = requireContext().getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)
        val currUser = sharedPrefs.getString("username","DEFAULT").toString()
        //getting users
        getUsers(currUser)

        // Get reference to the ImageButton
        val searchIcon: ImageButton = view.findViewById(R.id.searchIcon)

        // Set OnClickListener for the ImageButton
        searchIcon.setOnClickListener {
            // Get the username from the EditText
            val etSearch: EditText = view.findViewById(R.id.etSearch)
            val usernameToSearch = etSearch.text.toString().trim()

            if (usernameToSearch.isNotEmpty()) {
                // Search for the username in Firestore
                searchUsernameInFirestore(currUser,usernameToSearch)
            } else {
                // Handle empty username
                getUsers(currUser)
            }
        }
    }

    private fun getUsers(currUser: String){
        DataGenerator.generateUsers(currUser){
            val users = it
            setupView(users)
        }
    }
    private fun searchUsernameInFirestore(currUser: String,username: String) {

        DataGenerator.searchUser(currUser,username){
            val users = it

            setupView(users)
        }
    }

    private fun setupView(users:ArrayList<UserModel>){
        val itemAdapter = UserListAdapter(users, listener)

        val recyclerView: RecyclerView = requireView().findViewById(R.id.userList)
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