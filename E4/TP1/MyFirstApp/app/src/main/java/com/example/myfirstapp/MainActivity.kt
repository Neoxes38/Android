package com.example.myfirstapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // get reference to button
        val button_add_E = findViewById(R.id.button_add_E) as Button
        // set on-click listener
        button_add_E.setOnClickListener(object : View.OnClickListener{
            override fun onClick(v: View?) {
                //Your code here
                val text = findViewById(R.id.text) as TextView
                var str: String = text.text.toString()
                str += "E"
                text.text = str
            }
        })
    }
}