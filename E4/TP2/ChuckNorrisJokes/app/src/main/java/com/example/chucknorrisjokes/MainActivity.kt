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
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.serialization.json.Json
import okhttp3.MediaType
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.create
import retrofit2.http.GET
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers


interface JokeApiService{
    @GET("jokes/random")
    fun giveMeAJoke() : Single<Joke>
}

object JokeApiServiceFactory{
    fun jokeService() : JokeApiService {
        val builder = Retrofit.Builder()
                        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                        .addConverterFactory(Json.asConverterFactory(MediaType.get("application/json")))
                        .baseUrl("https://api.chucknorris.io/")
                        .build()

        return builder.create(JokeApiService::class.java)
    }
}


class MainActivity : AppCompatActivity() {
    private var compo = CompositeDisposable()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Display the list in the logcat
        Log.d("list", jokes.joke_list.toString())

        val list = jokes.joke_list.map{Joke(categories = listOf(""),
                                            createdAt = "",
                                            iconUrl = "https://assets.chucknorris.host/img/avatar/chuck-norris.png",
                                            id = "",
                                            updatedAt = "",
                                            url = "",
                                            value = it)}

        val recycler = findViewById(R.id.recycler) as RecyclerView
        recycler.layoutManager = LinearLayoutManager(this)
        recycler.adapter = JokeAdapter(list)

        val joke_serv = JokeApiServiceFactory.jokeService()
        val joke: Single<Joke> = joke_serv.giveMeAJoke()
        val dispo = joke.subscribeOn(Schedulers.io()).subscribeBy(onError = {println("ERROR")}, onSuccess = {println("SASUGA AINZ SAMA")})
        compo.add(dispo)
    }
    // Notifies the JokeAdapter that data has changed
    fun setJokes( pJokes : List<String>){
        jokes.joke_list = pJokes
        recycler.adapter!!.notifyDataSetChanged()
    }

    override fun onDestroy(){
        super.onDestroy()
        compo.clear()
    }
}