package com.example.project3_filxster

import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.codepath.asynchttpclient.AsyncHttpClient
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import com.google.gson.Gson
import okhttp3.Headers
import org.json.JSONException

private const val TAG = "MainActivity"
private const val NOW_PLAYING_URL = "https://api.themoviedb.org/3/movie/now_playing?&api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed"

class MainActivity : AppCompatActivity() {

    private val movies = mutableListOf<Movie>()
    private lateinit var rvMovies: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        rvMovies = findViewById(R.id.rvMovies)
        val movieAdapter = MovieAdapter(this, movies)
        rvMovies.adapter = movieAdapter

        val orientation = resources.configuration.orientation
        val spanCount = if (orientation == Configuration.ORIENTATION_LANDSCAPE) 2 else 1
        rvMovies.layoutManager = GridLayoutManager(this, spanCount)

        val client = AsyncHttpClient()
        client.get(NOW_PLAYING_URL, object : JsonHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Headers, json: JSON) {
                Log.i(TAG, "onSuccess: JSON data $json")
                try {
                    val movieResponse = Gson().fromJson(json.jsonObject.toString(), MovieResponse::class.java)
                    if (movieResponse.results != null) {
                        movies.addAll(movieResponse.results)
                        movieAdapter.notifyDataSetChanged()
                        Log.i(TAG, "Loaded ${movies.size} movies")
                    }
                } catch (e: JSONException) {
                    Log.e(TAG, "Hit JSON exception", e)
                }
            }

            override fun onFailure(statusCode: Int, headers: Headers, response: String?, throwable: Throwable?) {
                Log.e(TAG, "onFailure: $statusCode", throwable)
            }
        })
    }
}
