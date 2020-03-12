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
import io.reactivex.Single.just
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.serialization.json.Json
import okhttp3.MediaType
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.create
import retrofit2.http.GET
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers


interface JokeApiService {
    @GET("jokes/random")
    fun giveMeAJoke(): Single<Joke>
}

object JokeApiServiceFactory {
    fun jokeService(): JokeApiService {
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

        val adapter = JokeAdapter(mutableListOf())

        val jokeServ = JokeApiServiceFactory.jokeService()
        val joke: Single<Joke> = jokeServ.giveMeAJoke() // Get a joke

        val dispo: Disposable = joke.subscribeOn(Schedulers.io())
            .subscribeBy(
                onError = { println("ERROR") },
                onSuccess = {
                    println("YEEEEAAAAAAAHHHH")
                    adapter.addJoke(it)
                })

        compo.add(dispo)


        //Log.i("Joke : ", adapter.jokes[0].value)

        val recycler: RecyclerView = findViewById(R.id.recycler)
        recycler.layoutManager = LinearLayoutManager(this)
        recycler.adapter = adapter
    }

    override fun onDestroy() {
        super.onDestroy()
        compo.clear()
    }
}