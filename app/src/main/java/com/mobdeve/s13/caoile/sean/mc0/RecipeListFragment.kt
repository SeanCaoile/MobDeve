package com.mobdeve.s13.caoile.sean.mc0

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


class RecipeListFragment : Fragment(), RecipeListClickListener {
    lateinit var listener: RecipeListClickListener
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        listener = this
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_recipes, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // getting the recipes
        val recipes = DataGenerator.generateRecipes()
        Log.d("TAG", "Generating Recipes")
        Log.d("TAG", recipes.get(0).toString())

        // Assign recipes to ItemAdapter
        val itemAdapter = RecipeListAdapter(recipes, listener)

        // Set the LayoutManager that
        // this RecyclerView will use.
        val recyclerView: RecyclerView = view.findViewById(R.id.recipeListRV)
        recyclerView.layoutManager = LinearLayoutManager(context)

        // adapter instance is set to the
        // recyclerview to inflate the items.
        recyclerView.adapter = itemAdapter
    }

    override fun onRecipeListItemClick(view: View, recipe: RecipeModel, position: Int) {


        val intent = Intent(activity, RecipeActivity::class.java)
        intent.putExtra(RecipeActivity.NAME_KEY, recipe.recipeName)
        intent.putExtra(RecipeActivity.CREATOR_KEY, recipe.creator)
        intent.putExtra(RecipeActivity.INSTRUCTIONS_KEY, recipe.instructions)
        intent.putExtra(RecipeActivity.IMG_KEY, recipe.imageId)

        intent.putExtra(RecipeActivity.INGREDIENTS_KEY, recipe.ingredients)

        startActivity(intent)
    }
}