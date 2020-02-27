package com.example.chucknorrisjokes

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import android.util.Log // Used to write logs in the logcat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Display the list in the logcat
        Log.d("list", jokes.joke_list.toString());

        val recycler = findViewById(R.id.recycler) as RecyclerView
        recycler.layoutManager = LinearLayoutManager(this)
        recycler.adapter = JokeAdapter(jokes.joke_list)



        // Notifies the JokeAdapter that data has changed
        fun setJokes( pJokes : List<String>){
            jokes.joke_list = pJokes
            recycler.adapter!!.notifyDataSetChanged()
        }
    }
}