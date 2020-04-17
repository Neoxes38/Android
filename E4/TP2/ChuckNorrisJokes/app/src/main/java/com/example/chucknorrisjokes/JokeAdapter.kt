package com.example.chucknorrisjokes

import android.content.Intent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView

class JokeAdapter(var jokes: MutableList<Joke>, val onBottomReached: (JokeAdapter) -> Unit?): RecyclerView.Adapter<JokeAdapter.JokeViewHolder>() {

    class JokeViewHolder(val jokeView: JokeView): RecyclerView.ViewHolder(jokeView)

    // Shared jokes logs
    var logsShare: MutableList<String> = mutableListOf()
    // Fav jokes logs
    var logsStar: MutableList<String> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JokeViewHolder {
        val jokeViewCreated = JokeView(parent.context)

        // Inflate in JokeView doesn't take account of parent so we use LayoutParams
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
        val newTView = holder.jokeView.findViewById(R.id.joke_view) as TextView
        newTView.text = jokes[position].value

        // Behavior of the fav button
        val newFavB = holder.jokeView.findViewById(R.id.fav_button) as ImageButton
        newFavB.setOnClickListener(object : View.OnClickListener{

            override fun onClick(v: View) // When we click on the fav button
            {
                if (logsStar.contains(jokes[position].id)) // If the Joke has already been added to the list of favorite jokes
                {
                    logsStar.remove(jokes[position].id) // Remove the Joke from the list of favorite jokes
                    newFavB.setImageResource(R.drawable.fav_star) // Change the image to fav_star (default image for the fav button)
                }

                else // If the Joke is not in the list of favorite jokes
                {
                    logsStar.add(jokes[position].id) // Add the Joke's id to logs_star list
                    newFavB.setImageResource(R.drawable.clicked_fav_star) // Change the image to clicked_fav_star (image for when the button has been clicked)

                }
            }
        })

        // Behavior of the share button
        val newShareB = holder.jokeView.findViewById(R.id.share_button) as ImageButton
        newShareB.setOnClickListener(object: View.OnClickListener {

            override fun onClick(v: View) // When we click on the share button
            {
                logsShare.add(jokes[position].id) // Add the Joke's id to logs_share list

                // Share of the joke
                val sendIntent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT, jokes[position].value) //text to sent
                    type = "text/plain"
                }
                val shareIntent = Intent.createChooser(sendIntent, "Share" )
                startActivity(holder.jokeView.context,shareIntent,null)
            }
        })

        val newModel = JokeView.Model(newTView, newShareB, newFavB)

        holder.jokeView.setupView(newModel) // We update the view
    }

    override fun getItemCount(): Int {
        return jokes.count()
    }

    fun addJoke(joke: Joke){
        this.jokes.add(joke)
        this.notifyDataSetChanged()
    }
}