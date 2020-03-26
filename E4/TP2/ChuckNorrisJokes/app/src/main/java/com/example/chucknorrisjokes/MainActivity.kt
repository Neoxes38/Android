package com.example.chucknorrisjokes

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.view.View.*
import android.widget.ProgressBar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import kotlinx.serialization.json.Json
import okhttp3.MediaType
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.http.GET
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit


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

        val pBar: ProgressBar = findViewById(R.id.pBar) // We get the ProgressBar

        val adapter = JokeAdapter(mutableListOf()){
            val pBar: ProgressBar = findViewById(R.id.pBar) // We get the ProgressBar

            val jokeServ = JokeApiServiceFactory.jokeService()
            val joke: Single<Joke> = jokeServ.giveMeAJoke()

            val dispos: Disposable = joke
                .delay(1000, TimeUnit.MILLISECONDS)
                .repeat(10)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe{pBar.visibility = VISIBLE}
                .doOnTerminate{pBar.visibility = GONE}
                .subscribeBy(
                    onError = { println("ERROR") },
                    onNext = { j: Joke ->
                        println("YEEEEAAAAAAAHHHH")
                        it.addJoke(j)
                    })
            compo.add(dispos)
            Unit
        }

        val recycler: RecyclerView = findViewById(R.id.recycler) // We get the RecyclerView
        recycler.layoutManager = LinearLayoutManager(this)
        recycler.adapter = adapter

        adapter.onBottomReached(adapter)
        Thread.sleep(1000)
        adapter.onBottomReached(adapter)
    }


    override fun onDestroy() {
        super.onDestroy()
        compo.clear()
    }
}