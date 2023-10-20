package com.mobdeve.s13.caoile.sean.mc0

import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity

class IngredientNewActivity : AppCompatActivity(){

    private lateinit var backBtn: ImageButton
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_ingredient)

        backBtn = findViewById<View>(R.id.backBtn) as ImageButton

        val position = intent.getIntExtra(IngredientEditActivity.POSITION_KEY, 0)

        backBtn.setOnClickListener(View.OnClickListener {
            finish()
        })



    }
}