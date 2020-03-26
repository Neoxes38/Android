package com.example.chucknorrisjokes

import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater

class JokeAdapter(var jokes : MutableList<Joke>, val onBottomReached: (JokeAdapter) -> Unit?) : RecyclerView.Adapter<JokeAdapter.JokeViewHolder>() {

    class JokeViewHolder(val jokes_view : TextView) : RecyclerView.ViewHolder(jokes_view)

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JokeViewHolder {
            // Inflate the layout using LayoutInflater
            val textView = LayoutInflater.from(parent.context).inflate(R.layout.joke_layout, parent, false) as TextView

            return JokeViewHolder(textView)
        }

        override fun onBindViewHolder(holder: JokeViewHolder, position: Int) {
            holder.jokes_view.text = jokes[position].value
        }

        override fun getItemCount(): Int {
            return jokes.size
        }

    fun addJoke(joke : Joke){
        this.jokes.add(joke)
        this.notifyDataSetChanged()
    }
}