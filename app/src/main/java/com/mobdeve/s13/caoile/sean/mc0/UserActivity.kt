package com.mobdeve.s13.caoile.sean.mc0

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class UserActivity : AppCompatActivity (){

    companion object{
        const val USERNAME_KEY = "USERNAME_KEY"
        const val RECIPE_KEY = "RECIPE_KEY"
        const val IMG_KEY = "IMG_KEY"
        const val FAVDISH_KEY = "FAVDISH_KEY"
    }

    private lateinit var usernameTV : TextView
    private lateinit var recipeName: TextView
    private lateinit var recipeIV: ImageView
    private lateinit var backBtn: ImageButton
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_page)

        usernameTV = findViewById<View>(R.id.username) as TextView
        backBtn = findViewById<View>(R.id.backBtn) as ImageButton
        //favDishes = findViewById<View>(R.id.favDishes) as ArrayList<UserModel>

        usernameTV.text = intent.getStringExtra(UserActivity.USERNAME_KEY)
        val favDishes = intent.getSerializableExtra(UserActivity.FAVDISH_KEY) as? ArrayList<RecipeModel>

        if (favDishes != null){
            Log.d("TAG", "Generating Fav Dishes")
            val recyclerView = findViewById<RecyclerView>(R.id.favDishes)
            val adapter = HomeAdapter(favDishes)
            
            recyclerView.layoutManager = LinearLayoutManager(this)
            recyclerView.adapter = adapter
        }

        backBtn.setOnClickListener(View.OnClickListener {
            finish()
        })

    }
}