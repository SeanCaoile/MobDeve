package com.mobdeve.s13.caoile.sean.mc0

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton
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
    private lateinit var backBtn: ImageButton
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recipe)

        foodNameTv = findViewById<View>(R.id.foodNameTv) as TextView
        food_creatorTv = findViewById<View>(R.id.food_creatorTv) as TextView
        instructionsTv = findViewById<View>(R.id.instructionsTv) as TextView
        recipeImg = findViewById<View>(R.id.recipeImg) as ImageView
        backBtn = findViewById<View>(R.id.backBtn) as ImageButton

        foodNameTv.text = intent.getStringExtra(RecipeActivity.NAME_KEY)
        food_creatorTv.text = intent.getStringExtra(RecipeActivity.CREATOR_KEY)
        instructionsTv.text = intent.getStringExtra(RecipeActivity.INSTRUCTIONS_KEY)
        recipeImg.setImageResource(intent.getIntExtra(RecipeActivity.IMG_KEY, 0))

        backBtn.setOnClickListener(View.OnClickListener {
            /*  TODO:
             *      1. Declare a new Intent
             *      2. Place the position into the Intent
             *      3. Set the result as OK passing the intent
             *      4. Properly finish the activity
             *      NOTE: We're passing back the position as we need to know what to delete in
             *            our data / ArrayList
             * */

            finish()


        })
        

    }
}