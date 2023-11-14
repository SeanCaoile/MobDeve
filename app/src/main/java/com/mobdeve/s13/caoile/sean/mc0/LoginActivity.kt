package com.mobdeve.s13.caoile.sean.mc0

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import at.favre.lib.crypto.bcrypt.BCrypt

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Initialize Firebase Authentication and Firestore
        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        val usernameEditText = findViewById<EditText>(R.id.usernameEdit)
        val passwordEditText = findViewById<EditText>(R.id.enterPW)
        val loginBtn = findViewById<Button>(R.id.loginBtn)

        loginBtn.setOnClickListener {
            val username = usernameEditText.text.toString()
            val password = passwordEditText.text.toString()

            if (username.isNotEmpty() && password.isNotEmpty()) {
                // Check if the user exists in the Firestore collection
                checkUserExistence(username, password, usernameEditText, passwordEditText)
            } else {
                username.takeIf { it.isNullOrEmpty() }?.let { usernameEditText.error = "This field is required." }
                password.takeIf { it.isNullOrEmpty() }?.let { passwordEditText.error = "This field is required." }
            }
        }

        val backButton: ImageButton = findViewById(R.id.backBtn)
        backButton.setOnClickListener {
            // Go back to the previous page (HomeFragment) without saving changes
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

    private fun checkUserExistence(username: String, password: String, usernameEditText: EditText, passwordEditText: EditText) {
        val usersCollection = firestore.collection("users")

        usersCollection
            .whereEqualTo("username", username)
            .get()
            .addOnSuccessListener { querySnapshot ->
                if (!querySnapshot.isEmpty) {
                    // User exists in the Firestore collection
                    // Now, you can check the password or perform authentication as needed
                    val storedPassword = querySnapshot.documents[0]["password"].toString()

                    // Use bcrypt to verify the entered password against the stored hashed password
                    if (BCrypt.verifyer().verify(password.toCharArray(), storedPassword).verified) {
                        // Passwords match, login successful
                        Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show()
                        // Add your code to navigate to the next screen or perform other actions
                        val intent = Intent(this, HomePage::class.java)
                        intent.putExtra("name", username)
                        startActivity(intent)
                    } else {
                        passwordEditText.error = "Incorrect password."
                    }
                } else {
                    usernameEditText.error = "User does not exist."
                }
            }
            .addOnFailureListener { exception ->
                Toast.makeText(this, "Error: ${exception.message}", Toast.LENGTH_SHORT).show()
            }
    }
}
