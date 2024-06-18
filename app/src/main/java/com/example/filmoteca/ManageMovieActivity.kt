package com.example.filmoteca

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
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

        // Initialize EditTexts
        editNameMovie = findViewById(R.id.editNameMovieNew)
        editDirector = findViewById(R.id.editDirectorNew)
        editAgeMin = findViewById(R.id.editAgeMinNew)
        editReleaseDate = findViewById(R.id.editReleaseDateNew)
        editSinopse = findViewById(R.id.editSinopseNew)

        // Get data from intent
        val movieName = intent.getStringExtra("movie_name") ?: ""
        val movieSynopsis = intent.getStringExtra("movie_synopsis") ?: ""
        val movieDirector = intent.getStringExtra("movie_director") ?: ""
        val ageMin = intent.getIntExtra("ageMin", 0)
        val releaseDate = intent.getStringExtra("releaseDate") ?: ""

        // Set data to EditTexts
        editNameMovie.setText(movieName)
        editDirector.setText(movieDirector)
        editSinopse.setText(movieSynopsis)
        editAgeMin.setText(ageMin.toString())
        editReleaseDate.setText(releaseDate)

        // Initialize Buttons
        btnUpdateMovie = findViewById(R.id.btnUpdateMovie)
        btnRemoveMovie = findViewById(R.id.imageButton3)
        btnAddToFavorites = findViewById(R.id.imageButton)
        btnAddToLiked = findViewById(R.id.imageButton2)
        btnWatchLater = findViewById(R.id.imageButton4)

        // Set Click Listeners
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

        // Verifique se todos os campos necessários estão preenchidos
        if (name.isEmpty() || director.isEmpty() || releaseDate.isEmpty() || synopsis.isEmpty()) {
            Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_SHORT).show()
            return
        }

        // Crie um mapa com os dados atualizados
        val updatedMovie = hashMapOf(
            "name" to name,
            "director" to director,
            "ageMin" to ageMin,
            "releaseDate" to releaseDate,
            "synopsis" to synopsis
        ).toMap() // Converta explicitamente para Map<String, Any>

        // Realize uma consulta para encontrar o documento pelo nome do filme
        db.collection("movies")
            .whereEqualTo("name", name)
            .get()
            .addOnSuccessListener { querySnapshot ->
                if (querySnapshot.documents.isNotEmpty()) {
                    // Obtém o primeiro documento encontrado (assumindo que há apenas um com o mesmo nome)
                    val movieId = querySnapshot.documents.first().id

                    // Atualize os dados no Firestore
                    db.collection("movies")
                        .document(movieId)
                        .update(updatedMovie)
                        .addOnSuccessListener {
                            Toast.makeText(this, "Filme atualizado com sucesso", Toast.LENGTH_SHORT).show()
                            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                            startActivity(intent)
                            finish() // Encerre a atividade após a atualização
                        }
                        .addOnFailureListener { e ->
                            Toast.makeText(this, "Erro ao atualizar filme: ${e.message}", Toast.LENGTH_SHORT).show()
                        }
                } else {
                    Toast.makeText(this, "Filme não encontrado", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Erro ao buscar filme: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun removeMovie() {
        val movieName = editNameMovie.text.toString()

        // Realize uma consulta para encontrar o documento pelo nome do filme
        db.collection("movies")
            .whereEqualTo("name", movieName)
            .get()
            .addOnSuccessListener { querySnapshot ->
                if (querySnapshot.documents.isNotEmpty()) {
                    // Obtém o primeiro documento encontrado (assumindo que há apenas um com o mesmo nome)
                    val movieId = querySnapshot.documents.first().id

                    // Remove o documento do Firestore
                    db.collection("movies")
                        .document(movieId)
                        .delete()
                        .addOnSuccessListener {
                            Toast.makeText(this, "Filme removido com sucesso", Toast.LENGTH_SHORT).show()
                            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                            startActivity(intent)
                            finish()
                        }
                        .addOnFailureListener { e ->
                            Toast.makeText(this, "Erro ao remover filme: ${e.message}", Toast.LENGTH_SHORT).show()

                        }
                } else {
                    Toast.makeText(this, "Filme não encontrado", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Erro ao buscar filme: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun addToFavorites() {
        val name = editNameMovie.text.toString()
        val director = editDirector.text.toString()
        val ageMin = editAgeMin.text.toString()
        val releaseDate = editReleaseDate.text.toString()
        val synopsis = editSinopse.text.toString()

        val movie = Movie(name, synopsis, director, ageMin.toIntOrNull() ?: 0, releaseDate)

        // Adicionar o filme aos favoritos localmente usando o PreferenceManager
        PreferenceManager.addFavoriteMovie(this, movie)

        Toast.makeText(this, "Filme adicionado aos favoritos", Toast.LENGTH_SHORT).show()
    }
    private fun addToLiked() {
        // Implement logic to add the movie to liked
        Toast.makeText(this, "Filme adicionado aos curtidos", Toast.LENGTH_SHORT).show()
    }

    private fun watchLater() {
        // Implement logic to add the movie to watch later list
        Toast.makeText(this, "Filme adicionado para assistir mais tarde", Toast.LENGTH_SHORT).show()
    }
}
