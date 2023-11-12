package com.mobdeve.s13.caoile.sean.mc0;

import java.io.Serializable

class IngredientModel(name: String, quantity: Float, quantityType: String) : Serializable {
    var ingredient = name
        private set
    var quantity = quantity
        private set
    var measurement = quantityType
        private set

    override fun toString(): String {
        return "Ingredient{" +
                "name='" + ingredient + '\'' +
                ", quantity='" + quantity +
                ", quantityType='" + measurement +
                '}'
    }
}
