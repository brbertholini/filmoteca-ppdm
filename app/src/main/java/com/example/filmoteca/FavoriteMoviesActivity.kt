package com.example.filmoteca

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.filmoteca.databinding.LayoutActivityFavoriteMoviesBinding
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class FavoriteMoviesActivity : AppCompatActivity() {

    private lateinit var binding: LayoutActivityFavoriteMoviesBinding
    private lateinit var movieAdapter: MovieAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = LayoutActivityFavoriteMoviesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val btnGrid = findViewById<ImageButton>(R.id.btnGrid)
        val btnCheck = findViewById<ImageButton>(R.id.btnCheck)
        val btnLike = findViewById<ImageButton>(R.id.btnLike)

        btnLike.setOnClickListener {
            val intent = Intent(this, LikedMoviesActivity::class.java)
            startActivity(intent)
        }

        btnGrid.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }

        btnCheck.setOnClickListener {
            val intent = Intent(this, WatchLaterMoviesActivity::class.java)
            startActivity(intent)
        }

        movieAdapter = MovieAdapter(loadFavoriteMovies(), object : OnItemClickListener {
            override fun onItemClick(movie: Movie) {
            }
        })
        binding.recyclerViewMovies.adapter = movieAdapter
        binding.recyclerViewMovies.layoutManager = LinearLayoutManager(this)
    }

    private fun loadFavoriteMovies(): List<Movie> {
        return PreferenceManager.getFavoriteMovies(this)
    }
}
