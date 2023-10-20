package com.mobdeve.s13.caoile.sean.mc0

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class IngredientsFragment : Fragment(), IngredientsListListener {
    lateinit var listener: IngredientsListListener
    lateinit var newButton: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        listener = this
        return inflater.inflate(R.layout.fragment_ingredients, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // getting the recipes
        val ingredients = DataGenerator.generateIngredients()
//        Log.d("TAG", "Generating Recipes")
//        Log.d("TAG", ingredients.get(0).toString())

        // Assign recipes to ItemAdapter
        val itemAdapter = IngredientListAdapterWithButton(ingredients, listener)

        // Set the LayoutManager that
        // this RecyclerView will use.
        val recyclerView: RecyclerView = view.findViewById(R.id.ingredientsListRv)
        recyclerView.layoutManager = LinearLayoutManager(context)

        // adapter instance is set to the
        // recyclerview to inflate the items.
        recyclerView.adapter = itemAdapter

        newButton = activity?.findViewById<View>(R.id.addBtn) as Button
        newButton.setOnClickListener(View.OnClickListener {
            val intent = Intent(activity, IngredientNewActivity::class.java)

            startActivity(intent)
        })
    }

    override fun onIngredientsListItemClick(view: View, ingredient: IngredientModel, position: Int) {
        Toast.makeText(requireContext(), ingredient.name + "", Toast.LENGTH_SHORT).show()

        val intent = Intent(activity, IngredientEditActivity::class.java)
        intent.putExtra(IngredientEditActivity.NAME_KEY, ingredient.name)
        intent.putExtra(IngredientEditActivity.QUANTITY_KEY, ingredient.quantity.toString())
        intent.putExtra(IngredientEditActivity.TYPE_KEY, ingredient.quantityType)
//        Log.d("TAG", "Starting Ingredient Edit Activity")
        startActivity(intent)
    }



}