package com.example.filmoteca

import android.content.Intent
import android.os.Bundle
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

        binding.buttonAddMovie.setOnClickListener {
            val intent = Intent(this, AddMovieActivity::class.java)
            startActivity(intent)
        }

        movieAdapter = MovieAdapter(mutableListOf())
        binding.recyclerViewMovies.adapter = movieAdapter
        binding.recyclerViewMovies.layoutManager = LinearLayoutManager(this)

        fetchMovies()
    }

    private fun fetchMovies() {
        db.collection("movies").get().addOnSuccessListener { result ->
            val movies = result.map { document ->
                Movie(
                    name = document.getString("name") ?: "",
                    synopsis = document.getString("synopsis") ?: ""
                )
            }
            movieAdapter.updateMovies(movies)
        }
    }
}
