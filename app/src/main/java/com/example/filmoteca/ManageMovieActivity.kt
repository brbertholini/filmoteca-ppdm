package com.example.filmoteca

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore

class ManageMovieActivity : AppCompatActivity() {
    private val db = FirebaseFirestore.getInstance()


    private lateinit var editNameMovie: EditText
    private lateinit var editDirector: EditText
    private lateinit var editAgeMin: EditText
    private lateinit var editReleaseDate: EditText
    private lateinit var editSinopse: EditText
    private lateinit var btnUpdateMovie: Button
    private lateinit var btnRemoveMovie: ImageButton
    private lateinit var btnAddToFavorites: ImageButton
    private lateinit var btnAddToLiked: ImageButton
    private lateinit var btnWatchLater: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_activity_update_movie)

        editNameMovie = findViewById(R.id.editNameMovieNew)
        editDirector = findViewById(R.id.editDirectorNew)
        editAgeMin = findViewById(R.id.editAgeMinNew)
        editReleaseDate = findViewById(R.id.editReleaseDateNew)
        editSinopse = findViewById(R.id.editSinopseNew)

        val movieName = intent.getStringExtra("movie_name") ?: ""
        val movieSynopsis = intent.getStringExtra("movie_synopsis") ?: ""
        val movieDirector = intent.getStringExtra("movie_director") ?: ""
        val ageMin = intent.getIntExtra("ageMin", 0)
        val releaseDate = intent.getStringExtra("releaseDate") ?: ""

        editNameMovie.setText(movieName)
        editDirector.setText(movieDirector)
        editSinopse.setText(movieSynopsis)
        editAgeMin.setText(ageMin.toString())
        editReleaseDate.setText(releaseDate)

        btnUpdateMovie = findViewById(R.id.btnUpdateMovie)
        btnRemoveMovie = findViewById(R.id.imageButton3)
        btnAddToFavorites = findViewById(R.id.imageButton)
        btnAddToLiked = findViewById(R.id.imageButton2)
        btnWatchLater = findViewById(R.id.imageButton4)

        btnUpdateMovie.setOnClickListener { updateMovie() }
        btnRemoveMovie.setOnClickListener { removeMovie() }
        btnAddToFavorites.setOnClickListener { addToFavorites() }
        btnAddToLiked.setOnClickListener { addToLiked() }
        btnWatchLater.setOnClickListener { watchLater() }
    }

    private fun updateMovie() {
        val name = editNameMovie.text.toString()
        val director = editDirector.text.toString()
        val ageMin = editAgeMin.text.toString().toIntOrNull() ?: 0
        val releaseDate = editReleaseDate.text.toString()
        val synopsis = editSinopse.text.toString()

        if (name.isEmpty() || director.isEmpty() || releaseDate.isEmpty() || synopsis.isEmpty()) {
            Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_SHORT).show()
            return
        }

        val updatedMovie = hashMapOf(
            "name" to name,
            "director" to director,
            "ageMin" to ageMin,
            "releaseDate" to releaseDate,
            "synopsis" to synopsis
        ).toMap()

        db.collection("movies")
            .whereEqualTo("name", name)
            .get()
            .addOnSuccessListener { querySnapshot ->
                if (querySnapshot.documents.isNotEmpty()) {
                    val movieId = querySnapshot.documents.first().id

                    db.collection("movies")
                        .document(movieId)
                        .update(updatedMovie)
                        .addOnSuccessListener {
                            Toast.makeText(this, "Filme atualizado com sucesso", Toast.LENGTH_SHORT)
                                .show()
                            intent.flags =
                                Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                            startActivity(intent)
                            finish()
                        }
                        .addOnFailureListener { e ->
                            Toast.makeText(
                                this,
                                "Erro ao atualizar filme: ${e.message}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                } else {
                    Toast.makeText(this, "Filme não encontrado", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Erro ao buscar filme: ${e.message}", Toast.LENGTH_SHORT)
                    .show()
            }
    }

    private fun removeMovie() {
        val movieName = editNameMovie.text.toString()

        db.collection("movies")
            .whereEqualTo("name", movieName)
            .get()
            .addOnSuccessListener { querySnapshot ->
                if (querySnapshot.documents.isNotEmpty()) {
                    val movieId = querySnapshot.documents.first().id

                    db.collection("movies")
                        .document(movieId)
                        .delete()
                        .addOnSuccessListener {
                            Toast.makeText(this, "Filme removido com sucesso", Toast.LENGTH_SHORT)
                                .show()
                            intent.flags =
                                Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                            startActivity(intent)
                            finish()
                        }
                        .addOnFailureListener { e ->
                            Toast.makeText(
                                this,
                                "Erro ao remover filme: ${e.message}",
                                Toast.LENGTH_SHORT
                            ).show()

                        }
                } else {
                    Toast.makeText(this, "Filme não encontrado", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Erro ao buscar filme: ${e.message}", Toast.LENGTH_SHORT)
                    .show()
            }
    }

    private fun addToFavorites() {
        val name = editNameMovie.text.toString()
        val director = editDirector.text.toString()
        val ageMin = editAgeMin.text.toString()
        val releaseDate = editReleaseDate.text.toString()
        val synopsis = editSinopse.text.toString()

        val movie = Movie(name, synopsis, director, ageMin.toIntOrNull() ?: 0, releaseDate)

        PreferenceManager.addFavoriteMovie(this, movie)

        Toast.makeText(this, "Filme adicionado aos favoritos", Toast.LENGTH_SHORT).show()
    }

    private fun addToLiked() {
        val movieName = editNameMovie.text.toString()
        val directorName = editDirector.text.toString()
        val ageMin = editAgeMin.text.toString()
        val releaseDateString = editReleaseDate.text.toString()
        val synopsis = editSinopse.text.toString()

        val movieData = HashMap<String, Any>()
        movieData["name"] = movieName
        movieData["director"] = directorName
        movieData["ageMin"] = ageMin
        movieData["releaseDate"] = releaseDateString
        movieData["synopsis"] = synopsis

        val moviesRef = db!!.collection("likedMovies")

        moviesRef.add(movieData)
            .addOnSuccessListener { documentReference: DocumentReference? ->
                Toast.makeText(
                    this@ManageMovieActivity,
                    "Filme cadastrado na lista de filmes curtidos!",
                    Toast.LENGTH_SHORT
                ).show()
            }
            .addOnFailureListener { e: Exception? ->
                Log.w("ManageMovieActivity", "Error adding movie to Firestore", e)
                Toast.makeText(
                    this@ManageMovieActivity,
                    "Erro ao cadastrar filme na lista de filmes curtidos!",
                    Toast.LENGTH_SHORT
                )
                    .show()
            }
    }

    private fun watchLater() {
        val name = editNameMovie.text.toString()
        val director = editDirector.text.toString()
        val ageMin = editAgeMin.text.toString()
        val releaseDate = editReleaseDate.text.toString()
        val synopsis = editSinopse.text.toString()

        val movie = Movie(name, synopsis, director, ageMin.toIntOrNull() ?: 0, releaseDate)

        PreferenceManager.addWatchLaterMovies(this, movie)

        Toast.makeText(this, "Filme adicionado para assistir mais tarde", Toast.LENGTH_SHORT).show()
    }
}