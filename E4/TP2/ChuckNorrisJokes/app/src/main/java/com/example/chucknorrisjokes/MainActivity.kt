package com.example.chucknorrisjokes

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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
import android.view.ViewTreeObserver.OnScrollChangedListener
import kotlinx.serialization.json.JsonConfiguration
import kotlinx.serialization.list


interface JokeApiService {
    @GET("jokes/random")
    fun giveMeAJoke(): Single<Joke>
}

object JokeApiServiceFactory {
    fun jokeService(): JokeApiService {
        val builder = Retrofit.Builder()
            .baseUrl("https://api.chucknorris.io/")
            .addConverterFactory(Json.asConverterFactory(MediaType.get("application/json")))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()

        return builder.create(JokeApiService::class.java)
    }
}


class MainActivity: AppCompatActivity() {
    private var compo = CompositeDisposable()
    private var loading = false
    private var adapter = JokeAdapter(mutableListOf()){
        this.loading = false
        val pBar: ProgressBar = findViewById(R.id.pBar) // We get the ProgressBar

        val jokeService = JokeApiServiceFactory.jokeService()
        val joke: Single<Joke> = jokeService.giveMeAJoke()

        val disposable: Disposable = joke
            .delay(100, TimeUnit.MILLISECONDS)
            .repeat(10)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe{pBar.visibility = VISIBLE}
            .doOnTerminate{pBar.visibility = GONE
                this.loading = false}
            .subscribeBy(
                onError = { println("ERROR") },
                onNext = { j: Joke ->
                    it.addJoke(j)
                })
        compo.add(disposable)
        Unit
    }
    private val itemHelper = JokeTouchHelper(adapter::onJokeRemoved, adapter::onItemMoved)

    override fun onCreate(savedInstanceState: Bundle?) {

        // Call the super class onCreate to complete the creation of activity like the view hierarchy
        super.onCreate(savedInstanceState)

        // Set the user interface layout for this activity
        setContentView(R.layout.activity_main)

        val recycler: RecyclerView = findViewById(R.id.recycler) // We get the RecyclerView
        recycler.layoutManager = LinearLayoutManager(this)
        recycler.adapter = adapter
        itemHelper.attachToRecyclerView(recycler)

        recycler.viewTreeObserver.addOnScrollChangedListener(OnScrollChangedListener {
            if (!recycler.canScrollVertically(1) && !this.loading) {
                // Bottom reached
                adapter.onBottomReached(adapter)
            }
        })

        if (savedInstanceState != null) {
            val jokeSavedString = savedInstanceState.getString("jokeString")
            if(jokeSavedString != null){
                val json = Json(JsonConfiguration.Stable)
                val jokeSaved = json.parse(Joke.serializer().list, jokeSavedString)
                jokeSaved.forEach{adapter.addJoke(it)}
            }
        }
        else{
            this.loading = false
            val pBar: ProgressBar = findViewById(R.id.pBar) // We get the ProgressBar

            val jokeService = JokeApiServiceFactory.jokeService()
            val joke: Single<Joke> = jokeService.giveMeAJoke()

            val dispos: Disposable = joke
                .delay(100, TimeUnit.MILLISECONDS)
                .repeat(10)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe{pBar.visibility = VISIBLE}
                .doOnTerminate{pBar.visibility = GONE
                    this.loading = false}
                .subscribeBy(
                    onError = { println("ERROR") },
                    onNext = { j: Joke ->
                        adapter.addJoke(j)
                    })
            compo.add(dispos)
        }
    }

    // Invoked when the activity may be temporarily destroyed, save the instance state here
    override fun onSaveInstanceState(outState: Bundle) {
        val json = Json(JsonConfiguration.Stable)
        val jokeString = json.stringify(Joke.serializer().list, adapter.jokes)
        outState.putString("jokeString", jokeString)
        super.onSaveInstanceState(outState)
    }

    override fun onDestroy() {
        super.onDestroy()
        compo.clear()
    }
}