package com.mobdeve.s13.caoile.sean.mc0

import android.media.Image
import android.os.Bundle
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment

class RecipeFragment : Fragment() {


    private lateinit var foodNameTv: TextView
    private lateinit var food_creatorTv: TextView
    private lateinit var instructionsTv: TextView
    private lateinit var recipeImg: ImageView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_recipe, container, false)

        foodNameTv = view.findViewById<View>(R.id.foodNameTv) as TextView
        food_creatorTv = view.findViewById<View>(R.id.food_creatorTv) as TextView

        instructionsTv = view.findViewById<View>(R.id.instructionsTv) as TextView
        recipeImg = view.findViewById<View>(R.id.recipeImg) as ImageView




        return view
    }
}