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

        val usernameTextEt: EditText = view.findViewById(R.id.usernameEdit)
        usernameTextEt.setText(data)

        val oldPasswordEt: EditText = view.findViewById(R.id.oldpw)
        val newPasswordEt: EditText = view.findViewById(R.id.newpw)

        // Confirm button
        val confirmButton: Button = view.findViewById(R.id.confirmBtn)
        confirmButton.setOnClickListener {
            val updatedUsername = usernameTextEt.text.toString()
            val oldPassword = oldPasswordEt.text.toString()
            val newPassword = newPasswordEt.text.toString()

//            if (username.isNotEmpty() || username == "The Guru") {
//                if (password.length >= 5) {
//                    if (password == confirmPW) {
//                        // Check if the username already exists in Firebase Database
//                        Log.w("checking", "checking")
//                        database.collection("users")
//                            .document(username)
//                            .get()
//                            .addOnSuccessListener { document ->
//                                if (document.exists()) {
//                                    // Username already exists, show an error message
//                                    usernameEditText.error = "Username already exists."
//                                } else {
//                                    // Username does not exist, proceed with the registration process
//                                    // You can initiate the user registration here
//
//                                    // Call the function to register the user
//                                    registerUser(username, password)
//                                }
//                            }
//                            .addOnFailureListener { exception ->
//                                // Handle any errors that may occur during the query
//                                Log.e("query_error", "Error querying the database: $exception")
//                            }
//                    } else {
//                        // Passwords do not match, show an error message
//                        confirmPWEditText.error = "The passwords do not match"
//                    }
//                } else {
//                    passwordEditText.error = "The password must be at least 5 characters long."
//                }
//            } else {
//                // Handle invalid input
//                // You can show an error message or toast here
//                usernameEditText.error = "This field is required"
//            }


            if (updatedUsername.isNotEmpty() && oldPassword.isNotEmpty() && newPassword.isNotEmpty()) {
                fetchUserID(data.toString(), updatedUsername, oldPassword, newPassword)
            } else{
                updatedUsername.takeIf { it.isNullOrEmpty() }?.let { usernameTextEt.error = "This field is required." }
                oldPassword.takeIf { it.isNullOrEmpty() }?.let { oldPasswordEt.error = "This field is required." }
                newPassword.takeIf { it.isNullOrEmpty() }?.let { newPasswordEt.error = "This field is required." }
            }


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
