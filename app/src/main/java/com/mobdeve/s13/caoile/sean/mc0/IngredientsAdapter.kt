package com.mobdeve.s13.caoile.sean.mc0

import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.recyclerview.widget.RecyclerView

class IngredientsAdapter : RecyclerView.Adapter<IngredientsAdapter.IngredientViewHolder>() {

    private val ingredientsList = ArrayList<IngredientModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IngredientViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.add_ingredient_item, parent, false)
        return IngredientViewHolder(view)
    }

    override fun onBindViewHolder(holder: IngredientViewHolder, position: Int) {

    }

    override fun getItemCount(): Int = ingredientsList.size

    fun getIngredients(): ArrayList<IngredientModel> {
        return ingredientsList
    }

    fun addIngredient(ingredient: IngredientModel) {
        ingredientsList.add(ingredient)
        notifyDataSetChanged()
    }
    fun removeFirstEmptyIngredient() {
        if (ingredientsList.isNotEmpty()) {
            val firstIngredient = ingredientsList.first()
            if (firstIngredient.ingredient.isBlank() && firstIngredient.quantity == null && firstIngredient.measurement.isBlank()) {
                ingredientsList.remove(firstIngredient)
                notifyDataSetChanged()
            }
        }
    }
    inner class IngredientViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val etIngredientName: EditText = itemView.findViewById(R.id.ingredientName)
        private val etQuantity: EditText = itemView.findViewById(R.id.quantity)
        private val etMeasurement: EditText = itemView.findViewById(R.id.measurement)
        private val btnConfirm: Button = itemView.findViewById(R.id.btnConfirm)
        init {
            btnConfirm.setOnClickListener {
                val ingredient = IngredientModel(
                    etIngredientName.text.toString(),
                    etQuantity.text.toString().toFloat(),
                    etMeasurement.text.toString()
                )
                removeFirstEmptyIngredient()
                addIngredient(ingredient)
            }
        }
        fun bind(ingredient: IngredientModel) {
            etIngredientName.setText(ingredient.ingredient)
            etQuantity.setText(ingredient.quantity.toString())
            etMeasurement.setText(ingredient.measurement)
        }
    }
}
