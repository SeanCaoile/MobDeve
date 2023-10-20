package com.mobdeve.s13.caoile.sean.mc0

import android.os.Bundle
import android.text.SpannableStringBuilder
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity

class IngredientEditActivity : AppCompatActivity(){

    companion object{
        const val NAME_KEY = "NAME_KEY"
        const val QUANTITY_KEY = "QUANTITY_KEY"
        const val TYPE_KEY = "TYPE_KEY"
        const val POSITION_KEY = "POSITION_KEY"
    }

    private lateinit var nameET : EditText
    private lateinit var quantityET: EditText
    private lateinit var measurementET: EditText
    private lateinit var backBtn: ImageButton
    private lateinit var deleteBtn: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_ingredient)

        nameET = findViewById<View>(R.id.ingredientET) as EditText
        quantityET = findViewById<View>(R.id.quantityET) as EditText
        measurementET = findViewById<View>(R.id.measurementET) as EditText
        backBtn = findViewById<View>(R.id.backBtn) as ImageButton
        deleteBtn = findViewById<View>(R.id.deleteBtn) as Button


        nameET.text = SpannableStringBuilder(intent.getStringExtra(IngredientEditActivity.NAME_KEY))
        quantityET.text = SpannableStringBuilder(intent.getStringExtra(IngredientEditActivity.QUANTITY_KEY))
        measurementET.text = SpannableStringBuilder(intent.getStringExtra(IngredientEditActivity.TYPE_KEY))

        val position = intent.getIntExtra(IngredientEditActivity.POSITION_KEY, 0)

        backBtn.setOnClickListener(View.OnClickListener {
            finish()
        })

        deleteBtn.setOnClickListener(View.OnClickListener {
            finish()
        })
    }
}