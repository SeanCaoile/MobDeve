package com.mobdeve.s13.caoile.sean.mc0

import android.media.Image
import android.os.Bundle
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.Window
import android.widget.LinearLayout
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
        return inflater.inflate(R.layout.fragment_recipe, container, false)

    }

//    private fun showBottomDialog(){
//        val dialog = Dialog(requireContext())
//
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
//        dialog.setContentView(R.layout.bottomsheet_layout)
//
//        dialog.show()
//        dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
//        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
//        dialog.window?.attributes?.windowAnimations = R.style.DialogAnimation
//        dialog.window?.setGravity(Gravity.BOTTOM)
//    }
}