package com.example.filmoteca

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.filmoteca.databinding.LayoutActivityHomeBinding
import com.google.firebase.firestore.FirebaseFirestore

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: LayoutActivityHomeBinding
    private lateinit var movieAdapter: MovieAdapter
    private val db = FirebaseFirestore.getInstance()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = LayoutActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val btnLike = findViewById<ImageButton>(R.id.btnLike)
        val btnCheck = findViewById<ImageButton>(R.id.btnCheck)
        val btnBookmark = findViewById<ImageButton>(R.id.btnBookmark)

        btnBookmark.setOnClickListener {
            val intent = Intent(this, FavoriteMoviesActivity::class.java)
            startActivity(intent)
        }

        binding.buttonAddMovie.setOnClickListener {
            val intent = Intent(this, AddMovieActivity::class.java)
            startActivity(intent)
        }

        movieAdapter = MovieAdapter(emptyList(), object : OnItemClickListener {
            override fun onItemClick(movie: Movie) {
                openUpdateMovieActivity(movie)
            }
        })
        binding.recyclerViewMovies.adapter = movieAdapter
        binding.recyclerViewMovies.layoutManager = LinearLayoutManager(this)

        fetchMovies()
    }

    fun fetchMovies() {
        db.collection("movies").get().addOnSuccessListener { result ->
            val movies = result.map { document ->
                // Verifica se o campo "ageMin" existe e é um número válido
                val ageMin: Long? = document.getLong("ageMin")
                val ageMinInt = ageMin?.toInt() ?: 0 // Converte para Int, default para 0 se for null

                Movie(
                    name = document.getString("name") ?: "",
                    synopsis = document.getString("synopsis") ?: "",
                    director = document.getString("director") ?: "",
                    ageMin = ageMinInt,
                    releaseDate = document.getString("releaseDate") ?: ""
                )
            }
            movieAdapter.updateMovies(movies)
        }
    }

    private fun openUpdateMovieActivity(movie: Movie) {
        val intent = Intent(this, ManageMovieActivity::class.java)
        intent.putExtra("movie_name", movie.name)
        intent.putExtra("movie_synopsis", movie.synopsis)
        intent.putExtra("movie_director", movie.director)
        intent.putExtra("ageMin", movie.ageMin.toString()) // Convertido para String
        intent.putExtra("releaseDate", movie.releaseDate)
        startActivity(intent)
    }

}


