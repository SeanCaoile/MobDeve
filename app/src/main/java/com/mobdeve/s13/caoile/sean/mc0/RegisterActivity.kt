package com.mobdeve.s13.caoile.sean.mc0

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import at.favre.lib.crypto.bcrypt.BCrypt
import com.google.firebase.firestore.DocumentReference
import java.util.*

class RegisterActivity : AppCompatActivity() {

    private val database = FirebaseFirestore.getInstance()
    private val usersRef = database.collection("users")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val usernameEditText = findViewById<EditText>(R.id.usernameEdit)
        val passwordEditText = findViewById<EditText>(R.id.enterPW)
        val confirmPWEditText = findViewById<EditText>(R.id.reenterPW)
        val confirmBtn = findViewById<Button>(R.id.confirmBtn)

        val backBtn = findViewById<ImageButton>(R.id.backBtn)
        backBtn.setOnClickListener{
            finish()
        }
        confirmBtn.setOnClickListener {
            val username = usernameEditText.text.toString()
            val password = passwordEditText.text.toString()
            val confirmPW = confirmPWEditText.text.toString()

            if (username.isNotEmpty() && username != "The Guru") {
                if (password.length >= 5) {
                    if (password == confirmPW) {
                        // Check if the username already exists in Firebase Database
                        Log.w("checking", "checking")
                        database.collection("users")
                            .document(username)
                            .get()
                            .addOnSuccessListener { document ->
                                if (document.exists()) {
                                    // Username already exists
                                    usernameEditText.error = "Username already exists."
                                } else {
                                    // Username does not exist, proceed with the registration process
                                    registerUser(username, password)
                                }
                            }
                            .addOnFailureListener { exception ->
                                // Handle any errors that may occur during the query
                                Log.e("query_error", "Error querying the database: $exception")
                            }
                    } else {
                        // Passwords do not match
                        confirmPWEditText.error = "The passwords do not match"
                    }
                } else {
                    passwordEditText.error = "The password must be at least 5 characters long."
                }
            } else {
                usernameEditText.error = if (username.isNullOrEmpty()) "This field is required." else "\"The Guru\" is not a valid username"
            }
        }
    }

    private fun registerUser(username: String, password: String) {
        val hashedPassword = BCrypt.withDefaults().hashToString(12, password.toCharArray())
        Log.d("HashedPassword", "Hashed password: $hashedPassword")
        val userID = UUID.randomUUID().toString()
        val favorites = listOf<DocumentReference>()
        // Create the user in Firebase Firestore
        val user = hashMapOf(
            "userID" to userID,
            "username" to username,
            "password" to hashedPassword,
            "favorites" to favorites
        )

        usersRef.document(username)
            .set(user)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // User registered successfully
                    Log.w("userID", "userID $userID")
                    Log.w("regis", "Successful register")
                    showToast("Registration Successful")
                    finish()
                } else {
                    // Registration failed
                    Log.w("failed", "failed register")
                    showToast("Registration Failed")
                }
            }
    }
    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
