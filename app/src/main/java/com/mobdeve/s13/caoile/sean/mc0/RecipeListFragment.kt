package com.mobdeve.s13.caoile.sean.mc0

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class RecipeListFragment : Fragment(), RecipeListClickListener {
    lateinit var listener: RecipeListClickListener
    lateinit var favFilter: FloatingActionButton
    private var fabOn: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        listener = this
        return inflater.inflate(R.layout.fragment_recipes, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sharedPrefs = requireContext().getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)
        val currUser = sharedPrefs.getString("username","DEFAULT").toString()
        // getting the recipes
//        val recipes = DataGenerator.generateRecipes(currUser)

        this.favFilter = requireView().findViewById(R.id.floatingActionButton)
        DataGenerator.generateRecipes(currUser) {
            val recipes = it

            // Assign recipes to ItemAdapter
            val itemAdapter = RecipeListAdapter(recipes, listener)

            // Set the LayoutManager that
            // this RecyclerView will use.
            val recyclerView: RecyclerView = view.findViewById(R.id.recipeListRV)
            recyclerView.layoutManager = LinearLayoutManager(context)

            // adapter instance is set to the
            // recyclerview to inflate the items.
            recyclerView.adapter = itemAdapter


            favFilter.setOnClickListener(View.OnClickListener {
                if(fabOn == false) {
                    fabOn = true
                    DBDataGetter.getFavorites(currUser) {
                        val updatedRecipes = it
                        val newItemAdapter = RecipeListAdapter(updatedRecipes, listener)
                        Log.d("TAG", it.toString())
                        Log.d("TAG", "DONE GETTING FAVS")
                        recyclerView.adapter = newItemAdapter

                        favFilter.setImageResource(R.drawable.staron)
                    }
                }
                else {
                    recyclerView.adapter = itemAdapter
                    fabOn = false
                    favFilter.setImageResource(R.drawable.staroff)
                }

            })
        }
    }

    override fun onRecipeListItemClick(view: View, recipe: RecipeModel, position: Int) {
        val intent = Intent(activity, RecipeActivity::class.java)
        intent.putExtra(RecipeActivity.NAME_KEY, recipe.recipeName)
        intent.putExtra(RecipeActivity.CREATOR_KEY, recipe.creator)
        intent.putExtra(RecipeActivity.INSTRUCTIONS_KEY, recipe.instructions)
        intent.putExtra(RecipeActivity.IMG_KEY, recipe.imageURL)

        intent.putExtra(RecipeActivity.INGREDIENTS_KEY, recipe.ingredients)

        startActivity(intent)
    }
}