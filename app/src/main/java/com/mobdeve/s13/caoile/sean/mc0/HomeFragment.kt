package com.mobdeve.s13.caoile.sean.mc0

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class HomeFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val currentUser: TextView = view.findViewById(R.id.username)
        val sharedPrefs = requireContext().getSharedPreferences("AppPrefs",Context.MODE_PRIVATE)
        val currUser = sharedPrefs.getString("username","DEFAULT").toString()
        DBDataGetter.getFavorites(currUser) {
            val dishes = it
            val itemAdapter = HomeAdapter(dishes)


            currentUser.text = currUser

            val recyclerView: RecyclerView = view.findViewById(R.id.favDishes)
            recyclerView.layoutManager = GridLayoutManager(context, 2)

            recyclerView.adapter = itemAdapter
        }



        val editBtn = view.findViewById<Button>(R.id.editBtn)

        editBtn.setOnClickListener {
            // Create an instance of the EditFragment or the fragment you want to navigate to
            val editFragment = EditFragment()

            // Create a Bundle to pass data
            val dataBundle = Bundle()
            val username = view.findViewById<TextView>(R.id.username).text.toString()
            dataBundle.putString("key", username) // Replace with your actual data

            // Set the arguments of the fragment with the data Bundle
            editFragment.arguments = dataBundle

            // Replace the current fragment with the EditFragment
            val fragmentManager: FragmentManager = requireActivity().supportFragmentManager
            val transaction: FragmentTransaction = fragmentManager.beginTransaction()
            transaction.replace(R.id.fragment_container, editFragment) // Use your container ID
            transaction.addToBackStack(null) // Add transaction to the back stack
            transaction.commit()
        }
    }
}