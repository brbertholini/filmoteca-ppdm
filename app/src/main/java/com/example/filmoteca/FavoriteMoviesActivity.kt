package com.example.filmoteca

import android.os.Bundle
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

        movieAdapter = MovieAdapter(loadFavoriteMovies(), object : OnItemClickListener {
            override fun onItemClick(movie: Movie) {
                // Aqui você pode implementar a lógica para lidar com o clique no item, se necessário
            }
        })
        binding.recyclerViewMovies.adapter = movieAdapter
        binding.recyclerViewMovies.layoutManager = LinearLayoutManager(this)
    }

    private fun loadFavoriteMovies(): List<Movie> {
        // Carregar os filmes favoritos usando PreferenceManager
        return PreferenceManager.getFavoriteMovies(this)
    }
}
