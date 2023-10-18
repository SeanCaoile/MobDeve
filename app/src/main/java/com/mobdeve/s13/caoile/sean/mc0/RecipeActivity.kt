package com.mobdeve.s13.caoile.sean.mc0

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class RecipeActivity : AppCompatActivity()  {
    companion object {
        const val NAME_KEY = "NAME_KEY"
        const val CREATOR_KEY = "CREATOR_KEY"
        const val INGREDIENTS_KEY = "INGREDIENTS_KEY"
        const val INSTRUCTIONS_KEY = "INSTRUCTIONS_KEY"
        const val IMG_KEY = "IMG_KEY"
        const val POSITION_KEY = "POSITION_KEY"
    }

    private lateinit var foodNameTv: TextView
    private lateinit var food_creatorTv: TextView
    private lateinit var instructionsTv: TextView
    private lateinit var recipeImg: ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recipe)

        foodNameTv = findViewById<View>(R.id.foodNameTv) as TextView
        food_creatorTv = findViewById<View>(R.id.food_creatorTv) as TextView
        instructionsTv = findViewById<View>(R.id.instructionsTv) as TextView
        recipeImg = findViewById<View>(R.id.recipeImg) as ImageView

        foodNameTv.text = intent.getStringExtra(RecipeActivity.NAME_KEY)
        food_creatorTv.text = intent.getStringExtra(RecipeActivity.CREATOR_KEY)
        instructionsTv.text = intent.getStringExtra(RecipeActivity.INSTRUCTIONS_KEY)
        recipeImg.setImageResource(intent.getIntExtra(RecipeActivity.IMG_KEY, 0))


    }
}