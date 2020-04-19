package com.example.chucknorrisjokes

import android.content.Intent
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import java.util.*

class JokeAdapter(var jokes: MutableList<Joke>, val onBottomReached: (JokeAdapter) -> Unit?): RecyclerView.Adapter<JokeAdapter.JokeViewHolder>() {

    class JokeViewHolder(val jokeView: JokeView): RecyclerView.ViewHolder(jokeView)

    // Shared jokes logs
    var logsShare: MutableList<String> = mutableListOf()
    // Fav jokes logs
    var logsStar: MutableList<String> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JokeViewHolder {
        val jokeViewCreated = JokeView(parent.context)

        // We use LayoutParams to put everything at the right place
        val lp = RecyclerView.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        jokeViewCreated.layoutParams = lp

        return JokeViewHolder(jokeViewCreated)
    }

    override fun onBindViewHolder(holder: JokeViewHolder, position: Int) {
        if (position == this.itemCount - 1) {
            onBottomReached(this)
        }

        // We create a TextView with the new Joke
        val tView = holder.jokeView.findViewById(R.id.joke_view) as TextView
        tView.text = jokes[position].value

        // Behavior of the fav button
        val favB = holder.jokeView.findViewById(R.id.fav_button) as ImageButton
        favB.setOnClickListener {

            // If the Joke has already been added to the list of favorite jokes
            if (logsStar.contains(jokes[position].id)) {

                // Remove the Joke from the list of favorite jokes
                logsStar.remove(jokes[position].id)

                // Change the image to fav_star (default image for the fav button)
                favB.setImageResource(R.drawable.fav_star)
            }

            // If the Joke is not in the list of favorite jokes
            else {

                // Add the Joke's id to logs_star list
                logsStar.add(jokes[position].id)

                // Change the image to clicked_fav_star (image for when the button has been clicked)
                favB.setImageResource(R.drawable.clicked_fav_star)

            }
        }

        // Behavior of the share button
        val shareB = holder.jokeView.findViewById(R.id.share_button) as ImageButton
        shareB.setOnClickListener {

            // Add the Joke's id to logs_share list
            logsShare.add(jokes[position].id)

            // Share of the joke
            val sendIntent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, jokes[position].value) // Text to send
                type = "text/plain"
            }
            val shareIntent = Intent.createChooser(sendIntent, "Share" )
            startActivity(holder.jokeView.context,shareIntent,null)
        }

        val model = JokeView.Model(tView, shareB, favB)

        holder.jokeView.setupView(model) // We update the view
    }

    override fun getItemCount(): Int {
        return jokes.count()
    }

    fun addJoke(joke: Joke){
        this.jokes.add(joke)
        this.notifyDataSetChanged()
    }

    // Move a view from initialPosition to targetPosition
    fun onItemMoved(initialPosition: Int, targetPosition: Int) {
        // View must to go down
        if (initialPosition < targetPosition) {
            for (i in initialPosition until targetPosition )
            {
                Collections.swap(jokes, i,i+1)
            }
        }

        // View must go up
        else {
            for (i in initialPosition downTo  targetPosition+1 )
            {
                Collections.swap(jokes, i,i-1)
            }
        }
        notifyItemMoved(initialPosition, targetPosition)
    }


    fun onJokeRemoved(position: Int)
    {
        jokes.removeAt(position)
        notifyItemRemoved(position)
    }
}