package com.example.filmoteca

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class MovieAdapter(private var movies: List<Movie>, private val itemClickListener: OnItemClickListener) : RecyclerView.Adapter<MovieAdapter.MovieViewHolder>() {

    inner class MovieViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val titleTextView: TextView = itemView.findViewById(R.id.text_view_title)
        private val synopsisTextView: TextView = itemView.findViewById(R.id.text_view_synopsis)
        private val directorTextView: TextView = itemView.findViewById(R.id.directorText)

        fun bind(movie: Movie) {
            titleTextView.text = movie.name
            synopsisTextView.text = movie.synopsis
            directorTextView.text = movie.director

            itemView.setOnClickListener {
                itemClickListener.onItemClick(movie)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_movie, parent, false)
        return MovieViewHolder(view)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.bind(movies[position])
    }

    override fun getItemCount(): Int = movies.size

    fun updateMovies(newMovies: List<Movie>) {
        movies = newMovies
        notifyDataSetChanged()
    }
}



interface OnItemClickListener {
    fun onItemClick(movie: Movie)
}