package com.mobdeve.s13.caoile.sean.mc0

import android.content.Intent
import android.os.Bundle
import android.util.Log
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

            fetchUserID(data.toString(), updatedUsername, oldPassword, newPassword)
        }

        // Back button
        val backButton: ImageButton = view.findViewById(R.id.backBtn)
        backButton.setOnClickListener {
            // Go back to the previous page (HomeFragment) without saving changes
            goBackToHomeFragment()
        }

        return view
    }

    private fun fetchUserID(
        originalUsername: String,
        updatedUsername: String,
        oldPassword: String,
        newPassword: String
    ) {
        // Fetch user data using the original username
        usersRef.whereEqualTo("username", originalUsername)
            .get()
            .addOnSuccessListener { querySnapshot ->
                if (!querySnapshot.isEmpty) {
                    // User found, get the userID
                    val userID = querySnapshot.documents[0].id
                    Log.w("userID", "userID $userID")
                    // Check if the old password is correct before updating
                    checkUserOldPasswordAndUpdate(userID, updatedUsername, oldPassword, newPassword)
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
    private fun checkUserOldPasswordAndUpdate(
        userID: String,
        updatedUsername: String,
        oldPassword: String,
        newPassword: String
    ) {
        // Fetch user data using the updated username
        usersRef.document(userID)
            .get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    // User found, check if the old password is correct
                    val storedPassword = document.getString("password") ?: ""
                    if (BCrypt.verifyer().verify(oldPassword.toCharArray(), storedPassword).verified) {
                        // Old password is correct, update the password
                        updateUserData(userID, updatedUsername, newPassword)
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

    private fun updateUserData(userID: String, updatedUsername: String, newPassword: String) {
        // Hash the new password using bcrypt
        val hashedPassword = BCrypt.withDefaults().hashToString(12, newPassword.toCharArray())

        // Update the user's password in the database
        usersRef.document(userID)
            .update("username", updatedUsername, "password", hashedPassword)
            .addOnSuccessListener {
                // Update successful, show a success message
                showToast("User data updated successfully")
                // Go back to the previous page (HomeFragment)
                goBackToMainActivity()
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

    private fun goBackToMainActivity() {
        // Create an Intent to start MainActivity
        val intent = Intent(requireContext(), MainActivity::class.java)

        // Clear the back stack (remove all previous fragments)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

        // Start MainActivity
        startActivity(intent)

        // Finish the current activity (EditFragment)
        requireActivity().finish()
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }
}
