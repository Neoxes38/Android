package com.example.myfirstapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import android.util.Log // Used to write logs in the logcat

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Get reference to button
        val button_add_E = findViewById(R.id.button_add_E) as Button

        // Get reference to text
        val text = findViewById(R.id.main_text) as TextView

        // Get layout parameters of button_add_E
        var params = button_add_E.layoutParams as ConstraintLayout.LayoutParams
        // Change the layout constraints of button_add_E
        params.bottomToTop = text.id
        params.leftToLeft = text.id
        params.rightToRight = text.id
        // Update the layout of button_add_E
        button_add_E.requestLayout()


        val names = listOf("Alexandre", "Maÿlis", "Barbara", "Bruh", "Charlène")
        names.sortedBy{ it.length}
        // Prints the list in the loggcat
        Log.d("list", names.toString());

        val list_text = findViewById(R.id.list_text) as TextView
        var str_list: String = list_text.text.toString()
        str_list = names.toString()
        list_text.text = str_list



        // Set on-click listener
        button_add_E.setOnClickListener(object : View.OnClickListener{
            // Do things when we click on button_add_E
            override fun onClick(v: View?) {
                val rand_name = names.random()
                // Get the text of main_text
                val main_text = findViewById(R.id.main_text) as TextView
                // Converts the text into a String
                var main_str: String = main_text.text.toString()
                main_str = rand_name
                main_text.text = main_str
            }
        })
    }
}