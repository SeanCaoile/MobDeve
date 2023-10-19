package com.mobdeve.s13.caoile.sean.mc0

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton

class EditFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_edit, container, false)

        // Retrieve data passed from HomeFragment
        val data = arguments?.getString("key")

        // Display the data in an EditText
        val editText: EditText = view.findViewById(R.id.usernameEdit)
        editText.setText(data)

        // Confirm button
        val confirmButton: Button = view.findViewById(R.id.confirmBtn)
        confirmButton.setOnClickListener {
            // Perform actions to save or confirm the edits

            // Go back to the previous page (HomeFragment)
            goBackToHomeFragment()
        }

        // Back button
        val backButton: ImageButton = view.findViewById(R.id.backBtn)
        backButton.setOnClickListener {
            // Go back to the previous page (HomeFragment) without saving changes
            goBackToHomeFragment()
        }

        return view
    }

    private fun goBackToHomeFragment() {
        // Navigate back to the previous fragment (HomeFragment)
        val fragmentManager: FragmentManager = requireActivity().supportFragmentManager
        fragmentManager.popBackStack()
    }
}
