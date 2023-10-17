package com.mobdeve.s13.caoile.sean.mc0

import android.os.Bundle
import android.provider.ContactsContract.Data
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class RecipeListFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
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
        val itemAdapter = RecipeListAdapter(recipes)

        // Set the LayoutManager that
        // this RecyclerView will use.
        val recyclerView: RecyclerView = view.findViewById(R.id.recipeListRV)
        recyclerView.layoutManager = LinearLayoutManager(context)

        // adapter instance is set to the
        // recyclerview to inflate the items.
        recyclerView.adapter = itemAdapter
    }
}