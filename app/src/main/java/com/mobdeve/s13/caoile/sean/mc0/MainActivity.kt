package com.mobdeve.s13.caoile.sean.mc0

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.widget.Button
import com.google.firebase.Firebase

class MainActivity : AppCompatActivity() {
    lateinit var auth : Firebase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val login: Button = findViewById(R.id.login)
        login.setOnClickListener {
            val intent = Intent(this,HomePage::class.java)
            startActivity(intent)
        }

    }

}