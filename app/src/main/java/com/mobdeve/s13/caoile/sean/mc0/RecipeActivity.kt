package com.mobdeve.s13.caoile.sean.mc0

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.PopupWindow
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.floatingactionbutton.FloatingActionButton

class RecipeActivity : AppCompatActivity()  {
    companion object {
        const val NAME_KEY = "NAME_KEY"
        const val CREATOR_KEY = "CREATOR_KEY"
        const val INGREDIENTS_KEY = "INGREDIENTS_KEY"
        const val INSTRUCTIONS_KEY = "INSTRUCTIONS_KEY"
        const val IMG_KEY = "IMG_KEY"
        const val CUR_ING_KEY = "CUR_ING_KEY"
        const val POSITION_KEY = "POSITION_KEY"
        const val ID_KEY = "ID_KEY"
    }

    private lateinit var foodNameTv: TextView
    private lateinit var foodCreatorTv: TextView
    private lateinit var instructionsTv: TextView
    private lateinit var recipeImg: ImageView
    private lateinit var backBtn: ImageButton
    private lateinit var viewIngredBtn: FloatingActionButton
    private lateinit var favBtn: ImageButton

    private lateinit var popupWindow: PopupWindow

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recipe)
        val sharedPrefs = this.getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)
        val currUser = sharedPrefs.getString("username","DEFAULT").toString()

        foodNameTv = findViewById<View>(R.id.foodNameTv) as TextView
        foodCreatorTv = findViewById<View>(R.id.food_creatorTv) as TextView
        instructionsTv = findViewById<View>(R.id.instructionsTv) as TextView
        recipeImg = findViewById<View>(R.id.recipeImg) as ImageView
        backBtn = findViewById<View>(R.id.backBtn) as ImageButton
        viewIngredBtn = findViewById<View>(R.id.floatingActionButton) as FloatingActionButton
        favBtn = findViewById<View>(R.id.saveBtn) as ImageButton

        foodNameTv.text = intent.getStringExtra(NAME_KEY)
        foodCreatorTv.text = intent.getStringExtra(CREATOR_KEY)
        instructionsTv.text = intent.getStringExtra(INSTRUCTIONS_KEY)

        Glide.with(this)
            .load(intent.getStringExtra(IMG_KEY))
            .into(recipeImg)



        var favorited = false


            //favorited = !favorited
            DBDataGetter.getCurrentRecipeReference(foodNameTv.text.toString(), foodCreatorTv.text.toString().drop(4))
            {
                DBDataGetter.checkIfFavorited(it, currUser) {
                    if(it == true) {
                        favBtn.setImageResource(R.drawable.ic_like_on_foreground)
                        favorited = true
                    }
                    else {
                        favBtn.setImageResource(R.drawable.ic_like_off_foreground)
                        favorited = false
                    }
                }
                val ingredients = intent.getSerializableExtra(RecipeActivity.INGREDIENTS_KEY) as? ArrayList<IngredientModel>
                Log.d("TAG", "Adding Ingredients")
                Log.d("TAG", ingredients?.get(0).toString())
                if (ingredients != null){

                    val recyclerView = findViewById<RecyclerView>(R.id.ingredientRV)
                    val adapter = RecipeIngredientsAdapter(true,ingredients)

                    recyclerView.layoutManager = LinearLayoutManager(this)
                    recyclerView.adapter = adapter
                }

                backBtn.setOnClickListener{
                    finish()
                }

                favBtn.setOnClickListener {
                    Log.w("Tag", "Current favorited status is " + favorited.toString())
                    if (favorited == false) {
                        favBtn.setImageResource(R.drawable.ic_like_on_foreground)
                        Log.d("TAG", "finding Reference " + foodNameTv.text.toString())
                        DBDataGetter.getCurrentRecipeReference(foodNameTv.text.toString(), foodCreatorTv.text.toString().drop(4))
                        {
                            Log.d("TAG", "REEEEEEEEEEEEEEFFERENCE IS " + it.toString())
                            DBDataGetter.addFavoriteReference(it, currUser, false)
                            favorited = !favorited
                        }


                    } else {
                        favBtn.setImageResource(R.drawable.ic_like_off_foreground)
                        Log.d("TAG", "finding Reference " + foodNameTv.text.toString())
                        DBDataGetter.getCurrentRecipeReference(foodNameTv.text.toString(), foodCreatorTv.text.toString().drop(4))
                        {
                            Log.d("TAG", "REEEEEEEEEEEEEEFFERENCE IS " + it.toString())
                            DBDataGetter.addFavoriteReference(it, currUser, true)
                            favorited = !favorited
                        }

                    }

            }
        }

        viewIngredBtn.setOnClickListener {
            showIngredientsPopup()
        }
    }
    private fun showIngredientsPopup(){
        val overlay: FrameLayout = findViewById(R.id.overlay)
//        val curIngredients = DataGenerator.generateIngredients()
        val sharedPrefs = this.getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)
        val currUser = sharedPrefs.getString("username","DEFAULT").toString()


        DBDataGetter.getIngredients(currUser){
            var currIngredients = it

            val popupView = layoutInflater.inflate(R.layout.bottomsheet_layout, null)
            val recyclerView: RecyclerView = popupView.findViewById(R.id.ingredientsListRv)
            val adapter = RecipeIngredientsAdapter(false,currIngredients)
            recyclerView.layoutManager = LinearLayoutManager(this)
            recyclerView.adapter = adapter

            val cancelButton: ImageView = popupView.findViewById(R.id.cancel_button)

            popupWindow = PopupWindow(
                popupView,
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            popupWindow.animationStyle = R.style.DialogAnimation
            popupWindow.isFocusable = true

            popupWindow.setOnDismissListener {
                overlay.visibility = View.GONE
            }

            overlay.visibility = View.VISIBLE
            popupWindow.showAtLocation(popupView, Gravity.BOTTOM, 0, 0)

            cancelButton.setOnClickListener {
                popupWindow.dismiss()
                overlay.visibility = View.GONE
            }

            popupWindow.setOnDismissListener {
                overlay.visibility = View.GONE
            }
        }
    }
}