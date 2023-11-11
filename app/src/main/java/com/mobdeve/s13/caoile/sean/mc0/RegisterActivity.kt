package com.mobdeve.s13.caoile.sean.mc0

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import at.favre.lib.crypto.bcrypt.BCrypt
import java.util.*

class RegisterActivity : AppCompatActivity() {

    private val database = FirebaseFirestore.getInstance()
    private val usersRef = database.collection("users")
    private lateinit var auth: FirebaseAuth
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

            // Check if the username is not empty and password is valid
            if (username.isNotEmpty() || username == "The Guru") {
                if (password.length >= 5) {
                    if (password == confirmPW) {
                        // Check if the username already exists in Firebase Database
                        Log.w("checking", "checking")
                        database.collection("users")
                            .document(username)
                            .get()
                            .addOnSuccessListener { document ->
                                if (document.exists()) {
                                    // Username already exists, show an error message
                                    usernameEditText.error = "Username already exists."
                                } else {
                                    // Username does not exist, proceed with the registration process
                                    // You can initiate the user registration here

                                    // Call the function to register the user
                                    registerUser(username, password)
                                }
                            }
                            .addOnFailureListener { exception ->
                                // Handle any errors that may occur during the query
                                Log.e("query_error", "Error querying the database: $exception")
                            }
                    } else {
                        // Passwords do not match, show an error message
                        confirmPWEditText.error = "The passwords do not match"
                    }
                } else {
                    passwordEditText.error = "The password must be at least 5 characters long."
                }
            } else {
                // Handle invalid input
                // You can show an error message or toast here
                usernameEditText.error = "This field is required"
            }
        }
    }
    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
    // Function to register the user
    private fun registerUser(username: String, password: String) {
        // Hash the password using bcrypt
        val hashedPassword = BCrypt.withDefaults().hashToString(12, password.toCharArray())
        Log.d("HashedPassword", "Hashed password: $hashedPassword")
        val userID = UUID.randomUUID().toString()
        // Create the user in Firebase Firestore
        val user = hashMapOf(
            "userID" to userID,
            "username" to username,
            "password" to hashedPassword
            // Add other user data as needed
        )

        usersRef.document(username)
            .set(user)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // User registered successfully
                    // You can proceed to the next activity or perform other actions
                    Log.w("userID", "userID $userID")
                    Log.w("regis", "Successful register")
                    finish()
                } else {
                    // Registration failed
                    // You can show an error message or toast here
                    Log.w("failed", "failed register")
                }
            }
    }
}
