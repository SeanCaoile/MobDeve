package com.mobdeve.s13.caoile.sean.mc0

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class IngredientsFragment : Fragment(), IngredientsListListener {
    lateinit var listener: IngredientsListListener
    lateinit var newButton: Button
    lateinit var ingredientsList: ArrayList<IngredientModel>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        listener = this
        return inflater.inflate(R.layout.fragment_ingredients, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ingredientsList = arrayListOf<IngredientModel>()

        val sharedPrefs = requireContext().getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)
        val currUser = sharedPrefs.getString("username","DEFAULT").toString()
        Log.d("TAG", "CURRENT USER AAAAAAH")
        Log.d("TAG", currUser)

        DBDataGetter.getIngredients(currUser) {
            Log.d("TAG", "Getting Ingredients DB VER AAAAHHHHH")
            ingredientsList = it
            Log.d("TAG", ingredientsList.toString())

            val itemAdapter = IngredientListAdapterWithButton(ingredientsList, listener)

            val recyclerView: RecyclerView = view.findViewById(R.id.ingredientsListRv)
            recyclerView.layoutManager = LinearLayoutManager(context)

            recyclerView.adapter = itemAdapter

            newButton = activity?.findViewById<View>(R.id.addBtn) as Button
            newButton.setOnClickListener(View.OnClickListener {
                val intent = Intent(activity, IngredientNewActivity::class.java)
                startActivity(intent)
            })
        }
    }

    override fun onIngredientsListItemClick(view: View, ingredient: IngredientModel, position: Int) {
        val intent = Intent(activity, IngredientEditActivity::class.java)
        intent.putExtra(IngredientEditActivity.NAME_KEY, ingredient.ingredient)
        intent.putExtra(IngredientEditActivity.QUANTITY_KEY, ingredient.quantity.toString())
        intent.putExtra(IngredientEditActivity.TYPE_KEY, ingredient.measurement)

        startActivity(intent)
    }
    override fun onResume() {
        super.onResume()

        activity?.runOnUiThread {
            ingredientsList.clear()
            Log.d("RESUME", "Fragment onResume")

            val sharedPrefs = requireContext().getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)
            val currUser = sharedPrefs.getString("username","DEFAULT").toString()

            DBDataGetter.getIngredients(currUser) {retrievedIngredientsList ->
                ingredientsList = retrievedIngredientsList
                Log.d("newLIST", "$ingredientsList")

                val itemAdapter = IngredientListAdapterWithButton(ingredientsList, listener)

                val recyclerView: RecyclerView = requireView().findViewById(R.id.ingredientsListRv)
                recyclerView.layoutManager = LinearLayoutManager(context)

                recyclerView.adapter = itemAdapter
            }
        }
    }

    override fun onPause() {
        super.onPause()
        Log.d("PAUSE", "Fragment onPause")
    }
}