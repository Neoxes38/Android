package com.example.chucknorrisjokes

import android.content.Context
import android.graphics.ColorSpace
import android.view.LayoutInflater
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout



class JokeView @JvmOverloads constructor(context: Context) : ConstraintLayout(context) {

    data class Model (val textView : TextView,
                      val shareButton : ImageButton,
                      val favButton : ImageButton)


    init {
        LayoutInflater.from(context).inflate(R.layout.joke_layout, this, true)
    }


    fun setupView(model : Model) {

        // Update the TextView's text
        var tView: TextView = findViewById(R.id.joke_view)
        tView = model.textView

        // Update the share button
        var shareButton: ImageButton = findViewById(R.id.share_button)
        shareButton = model.shareButton

        // Update fav button
        var favButton: ImageButton = findViewById(R.id.fav_button)
        favButton = model.favButton
    }
}