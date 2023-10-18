package com.mobdeve.s13.caoile.sean.mc0

import java.io.Serializable

class UserModel(username: String, favDishes: ArrayList<RecipeModel>) : Serializable {
    var username = username
        private set
    var favDishes = favDishes
        private set

    override fun toString(): String {
        return "User{" +
                "name='" + username
                '}'
    }
}