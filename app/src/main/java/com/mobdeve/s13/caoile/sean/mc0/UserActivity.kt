package com.mobdeve.s13.caoile.sean.mc0

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class UserActivity : AppCompatActivity (){
    companion object{
        const val USERNAME_KEY = "USERNAME_KEY"
        const val FAVDISH_KEY = "FAVDISH_KEY"
    }

    private lateinit var usernameTV : TextView
    private lateinit var backBtn: ImageButton
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_page)

        usernameTV = findViewById<View>(R.id.username) as TextView
        backBtn = findViewById<View>(R.id.backBtn) as ImageButton

        usernameTV.text = intent.getStringExtra(USERNAME_KEY)
        val favDishes = intent.getSerializableExtra(FAVDISH_KEY) as? ArrayList<RecipeModel>

        if (favDishes != null){
            Log.d("TAG", "Generating Fav Dishes")
            val recyclerView = findViewById<RecyclerView>(R.id.favDishes)
            val adapter = HomeAdapter(favDishes)

            recyclerView.layoutManager = GridLayoutManager(this, 2)
            recyclerView.adapter = adapter
        }

        backBtn.setOnClickListener(View.OnClickListener {
            finish()
        })
    }
}