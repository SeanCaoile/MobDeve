package com.mobdeve.s13.caoile.sean.mc0;

class IngredientModel(name: String, quantity: Float, quantityType: String) {
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
