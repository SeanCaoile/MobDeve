package com.mobdeve.s13.caoile.sean.mc0;

import java.io.Serializable

class IngredientModel(name: String, quantity: Float, quantityType: String) : Serializable {
    var name = name
        private set
    var quantity = quantity
        private set

    var quantityType = quantityType
        private set

    override fun toString(): String {
        return "Ingredient{" +
                "name='" + name + '\'' +
                ", quantity='" + quantity +
                ", quantityType='" + quantityType +
                '}'
    }
}
