package com.example.filmoteca

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.filmoteca.databinding.LayoutActivityFavoriteMoviesBinding
import com.example.filmoteca.databinding.LayoutActivityWatchLaterBinding
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class WatchLaterMoviesActivity : AppCompatActivity() {

    private lateinit var binding: LayoutActivityWatchLaterBinding
    private lateinit var movieAdapter: MovieAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = LayoutActivityWatchLaterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val btnGrid = findViewById<ImageButton>(R.id.btnGrid)
        val btnLike = findViewById<ImageButton>(R.id.btnLike)
        val btnBookmark = findViewById<ImageButton>(R.id.btnBookmark)

        btnBookmark.setOnClickListener {
            val intent = Intent(this, FavoriteMoviesActivity::class.java)
            startActivity(intent)
        }

        btnGrid.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }

        btnLike.setOnClickListener {
            val intent = Intent(this, LikedMoviesActivity::class.java)
            startActivity(intent)
        }

        movieAdapter = MovieAdapter(loadWatchLaterMovies(), object : OnItemClickListener {
            override fun onItemClick(movie: Movie) {
            }
        })
        binding.recyclerViewMovies.adapter = movieAdapter
        binding.recyclerViewMovies.layoutManager = LinearLayoutManager(this)
    }

    private fun loadWatchLaterMovies(): List<Movie> {
        return PreferenceManager.getWatchLaterMovies(this)
    }
}
