package com.mobdeve.s13.caoile.sean.mc0

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import android.widget.Toast
import at.favre.lib.crypto.bcrypt.BCrypt
import com.google.firebase.firestore.FirebaseFirestore

class EditFragment : Fragment() {
    private val database = FirebaseFirestore.getInstance()
    private val usersRef = database.collection("users")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_edit, container, false)

        val data = arguments?.getString("key")

        val usernameText: EditText = view.findViewById(R.id.usernameEdit)
        usernameText.setText(data)

        val oldPassword: EditText = view.findViewById(R.id.oldpw)
        val newPassword: EditText = view.findViewById(R.id.newpw)

        // Confirm button
        val confirmButton: Button = view.findViewById(R.id.confirmBtn)
        confirmButton.setOnClickListener {
            val updatedUsername = usernameText.text.toString()
            val oldPassword = oldPassword.text.toString()
            val newPassword = newPassword.text.toString()

            // Check if old password is correct before updating
            checkUserOldPasswordAndUpdate(data.toString(), updatedUsername, oldPassword, newPassword)
        }

        // Back button
        val backButton: ImageButton = view.findViewById(R.id.backBtn)
        backButton.setOnClickListener {
            // Go back to the previous page (HomeFragment) without saving changes
            goBackToHomeFragment()
        }

        return view
    }

    private fun checkUserOldPasswordAndUpdate(
        usernameOG: String,
        updatedUsername: String,
        oldPassword: String,
        newPassword: String
    ) {
        // Fetch user data using the updated username
        usersRef.document(usernameOG)
            .get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    // User found, check if the old password is correct
                    val storedPassword = document.getString("password") ?: ""
                    if (BCrypt.verifyer().verify(oldPassword.toCharArray(), storedPassword).verified) {
                        // Old password is correct, update the password
                        updateUserData(usernameOG, updatedUsername, newPassword)
                    } else {
                        // Old password is incorrect, show an error message
                        showToast("Incorrect old password")
                    }
                } else {
                    // User not found, show an error message
                    showToast("User not found")
                }
            }
            .addOnFailureListener { exception ->
                // Handle any errors that may occur during the query
                showToast("Error: ${exception.message}")
            }
    }

    private fun updateUserData(usernameOG: String, updatedUsername: String, newPassword: String) {
        // Hash the new password using bcrypt
        val hashedPassword = BCrypt.withDefaults().hashToString(12, newPassword.toCharArray())

        // Update the user's password in the database
        usersRef.document(usernameOG)
            .update("username", updatedUsername, "password", hashedPassword)
            .addOnSuccessListener {
                // Update successful, show a success message
                showToast("User data updated successfully")
                // Go back to the previous page (HomeFragment)
                goBackToHomeFragment()
            }
            .addOnFailureListener { exception ->
                // Handle any errors that may occur during the update
                showToast("Error updating user data: ${exception.message}")
            }
    }

    private fun goBackToHomeFragment() {
        // Navigate back to the previous fragment (HomeFragment)
        val fragmentManager: FragmentManager = requireActivity().supportFragmentManager
        fragmentManager.popBackStack()
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }
}
