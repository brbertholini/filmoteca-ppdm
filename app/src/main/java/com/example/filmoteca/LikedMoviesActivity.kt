package com.example.filmoteca

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.filmoteca.databinding.LayoutActivityLikedMoviesBinding
import com.google.firebase.firestore.FirebaseFirestore

class LikedMoviesActivity : AppCompatActivity() {

    private lateinit var binding: LayoutActivityLikedMoviesBinding
    private lateinit var movieAdapter: MovieAdapter

    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = LayoutActivityLikedMoviesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val btnGrid = findViewById<ImageButton>(R.id.btnGrid)
        val btnCheck = findViewById<ImageButton>(R.id.btnCheck)
        val btnBookmark = findViewById<ImageButton>(R.id.btnBookmark)

        btnBookmark.setOnClickListener {
            val intent = Intent(this, FavoriteMoviesActivity::class.java)
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

        movieAdapter = MovieAdapter(emptyList(), object : OnItemClickListener {
            override fun onItemClick(movie: Movie) {
            }
        })
        binding.recyclerViewMovies.adapter = movieAdapter
        binding.recyclerViewMovies.layoutManager = LinearLayoutManager(this)

        loadLikedMovies()
    }

    private fun loadLikedMovies() {
        db.collection("likedMovies").get().addOnSuccessListener { result ->
            val movies = result.map { document ->
                val ageMin = document.getString("ageMin")
                val ageMinInt = ageMin?.toInt() ?: 0
                Movie(
                    name = document.getString("name") ?: "",
                    synopsis = document.getString("synopsis") ?: "",
                    director = document.getString("director") ?: "",
                    ageMin = ageMinInt,
                    releaseDate = document.getString("releaseDate") ?: ""
                )
            }
            movieAdapter.updateMovies(movies)
        }.addOnFailureListener { exception ->
        }
    }
}
