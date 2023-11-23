package com.mobdeve.s13.caoile.sean.mc0

import android.app.AlertDialog
import android.content.ContentValues
import android.content.Context
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
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore

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

        val confirmButton: Button = view.findViewById(R.id.confirmBtn)
        confirmButton.setOnClickListener {
            val updatedUsername = usernameTextEt.text.toString()
            val oldPassword = oldPasswordEt.text.toString()
            val newPassword = newPasswordEt.text.toString()

            if (updatedUsername.isNotEmpty() && updatedUsername != "The Guru") {
                if (newPassword.length >= 5) {
                    Log.w("checking", "checking")
                    database.collection("users")
                        .document(updatedUsername)
                        .get()
                        .addOnSuccessListener { document ->
                            if (document.exists()) {
                                usernameTextEt.error = "Username already exists."
                            } else {
                                if (oldPassword.isNotEmpty() && newPassword.isNotEmpty()) {
                                    fetchUserID(data.toString(), updatedUsername, oldPassword, newPassword)
                                } else{
                                    updatedUsername.takeIf { it.isNullOrEmpty() }?.let { usernameTextEt.error = "This field is required." }
                                    oldPassword.takeIf { it.isNullOrEmpty() }?.let { oldPasswordEt.error = "This field is required." }
                                    newPassword.takeIf { it.isNullOrEmpty() }?.let { newPasswordEt.error = "This field is required." }
                                }
                            }
                        }
                        .addOnFailureListener { exception ->
                            // Handle any errors that may occur during the query
                            Log.e("query_error", "Error querying the database: $exception")
                        }
                } else {
                    newPasswordEt.error = "The password must be at least 5 characters long."
                }
            } else {
                // Handle invalid input
                // You can show an error message or toast here
                usernameTextEt.error = if (updatedUsername.isNullOrEmpty()) "This field is required." else "\"The Guru\" is not a valid username"
            }
        }

        val deleteButton: Button = view.findViewById(R.id.deleteBtn)
        deleteButton.setOnClickListener {
            showPopup()
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

    private fun showPopup() {
        val alertDialogBuilder = AlertDialog.Builder(requireContext())

        // Set the title and message of the dialog
        alertDialogBuilder.setTitle("Delete Account")
        alertDialogBuilder.setMessage("Are you sure you want to delete this account?")

        // Set the positive button with a listener
        alertDialogBuilder.setPositiveButton("Delete account") { _, _ ->
            // Handle positive button click (if needed)

            val sharedPrefs = requireContext().getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)
            val currUser = sharedPrefs.getString("username","DEFAULT").toString()
            val firestore = Firebase.firestore

            firestore.collection("users")
                .whereEqualTo("username", currUser)
                .get()
                .addOnSuccessListener { result ->
                    for (document in result) {
                        val username = document.getString("username")

                        if (username != null && currUser == username){
                            document.reference.delete()
                                .addOnSuccessListener {
                                    // Document successfully deleted
                                    Log.d("Firestore", "Account successfully deleted!")
                                    goBackToMainActivity()
                                }
                                .addOnFailureListener { e ->
                                    // Handle errors
                                    Log.w("Firestore", "Error deleting document", e)
                                }
                        }
                    }
                }
                .addOnFailureListener { exception ->
                    Log.w(ContentValues.TAG, "Error getting documents",exception)
                }
        }

        // Set the negative button with a listener
        alertDialogBuilder.setNegativeButton("Cancel") { dialog, _ ->
            // Handle negative button click (if needed)
            dialog.cancel()
        }

        // Create and show the alert dialog
        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }
}
