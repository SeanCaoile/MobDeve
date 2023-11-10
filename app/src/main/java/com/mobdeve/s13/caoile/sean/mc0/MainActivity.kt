package com.mobdeve.s13.caoile.sean.mc0

import android.os.Bundle
import android.view.LayoutInflater
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.content.Intent
import android.view.View
import android.widget.Button

class MainActivity : AppCompatActivity() {
    private lateinit var loginBtn: Button
    private lateinit var regisBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        loginBtn = findViewById<Button>(R.id.login)

        loginBtn.setOnClickListener{
            val intent = Intent(this, LoginActivity::class.java)

            startActivity(intent)
        }

        regisBtn = findViewById<Button>(R.id.register)

        regisBtn.setOnClickListener(View.OnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)

            startActivity(intent)
        })
    }

}