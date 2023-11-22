package com.mobdeve.s13.caoile.sean.mc0

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class RecipeListFragment : Fragment(), RecipeListClickListener {
    lateinit var listener: RecipeListClickListener
    lateinit var favFilter: FloatingActionButton
    private var fabOn: Boolean = false

//    private val sharedPrefs = requireContext().getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)
//    private val currUser = sharedPrefs.getString("username","DEFAULT").toString()

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
        val btnAddRecipe = view.findViewById<Button>(R.id.btnAddRecipe)
        btnAddRecipe.setOnClickListener {
            val intent = Intent(requireContext(), AddRecipeActivity::class.java)

            intent.putExtra("USERNAME_KEY", currUser)

            startActivity(intent)
        }


        val searchIcon: ImageButton = view.findViewById(R.id.searchIcon)
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

        searchIcon.setOnClickListener {
            val etSearch: EditText = view.findViewById(R.id.etSearch)
            val recipeToSearch = etSearch.text.toString().trim()

            if (recipeToSearch.isNotEmpty()) {
                // Search for the username in Firestore
                searchRecipeInFirestore(currUser,recipeToSearch)
            } else {
                // Handle empty username
                getRecipes(currUser)
                fabOn = false
                favFilter.setImageResource(R.drawable.staroff)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        val etSearch: EditText = requireView().findViewById(R.id.etSearch)
        etSearch.setText("")

        val sharedPrefs = requireContext().getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)
        val currUser = sharedPrefs.getString("username","DEFAULT").toString()

        this.favFilter = requireView().findViewById(R.id.floatingActionButton)
        if(fabOn == true) {
            DBDataGetter.getFavorites(currUser) {
                val updatedRecipes = it

                val newItemAdapter = RecipeListAdapter(updatedRecipes, listener)
                val recyclerView: RecyclerView = requireView().findViewById(R.id.recipeListRV)
                recyclerView.layoutManager = LinearLayoutManager(context)
                recyclerView.adapter = newItemAdapter

                favFilter.setImageResource(R.drawable.staron)
            }
        }
        else {
            DataGenerator.generateRecipes(currUser) {
                val recipes = it
                val itemAdapter = RecipeListAdapter(recipes, listener)
                val recyclerView: RecyclerView = requireView().findViewById(R.id.recipeListRV)
                recyclerView.layoutManager = LinearLayoutManager(context)
                recyclerView.adapter = itemAdapter

                fabOn = false
                favFilter.setImageResource(R.drawable.staroff)
            }
        }

    }

    private fun getRecipes(currUser: String){
        DataGenerator.generateRecipes(currUser){
            val recipes = it
            setupView(recipes)
        }
    }
    private fun searchRecipeInFirestore(searchRecipe: String, currUser: String) {

        DataGenerator.searchRecipe(currUser,currUser){
            val recipes = it

            setupView(recipes)
        }
    }

    private fun setupView(recipes:ArrayList<RecipeModel>){
        val itemAdapter = RecipeListAdapter(recipes, listener)

        val recyclerView: RecyclerView = requireView().findViewById(R.id.recipeListRV)
        recyclerView.layoutManager = LinearLayoutManager(context)

        recyclerView.adapter = itemAdapter
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
