package com.example.filmoteca

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.filmoteca.databinding.LayoutActivityAddMovieBinding
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore


class AddMovieActivity : AppCompatActivity() {
    private var binding: LayoutActivityAddMovieBinding? = null
    private var db: FirebaseFirestore? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = LayoutActivityAddMovieBinding.inflate(layoutInflater)
        setContentView(binding!!.root)

        db = FirebaseFirestore.getInstance()

        binding!!.btnSaveMovie.setOnClickListener { v -> saveMovieData() }
    }
    private fun isValidMovieName(movieName: String): Boolean {
        val regex = "[A-Za-z0-9 \\-\\_\\']+"
        return movieName.matches(regex.toRegex())
    }

    private fun isValidName(name: String): Boolean {
        val regex = "[A-Za-z0-9 \\-\\_,.'\"]+"
        return name.matches(regex.toRegex())
    }

    private fun isValidDateFormat(dateString: String): Boolean {
        val regex = "^(0[1-9]|[12][0-9]|3[01])/(0[1-9]|1[012])/(19|20)\\d{2}$"
        return dateString.matches(regex.toRegex())
    }
    private fun saveMovieData() {
        val movieName = binding!!.editNameMovie.getText().toString().trim()
        val directorName = binding!!.editDirector.getText().toString().trim()
        val ageMinString = binding!!.editAgeMin.getText().toString().trim()
        val releaseDateString = binding!!.editReleaseDate.getText().toString().trim()
        val synopsis = binding!!.editSinopse.getText().toString().trim()

        if (movieName.isEmpty() || directorName.isEmpty() || ageMinString.isEmpty() ||
            releaseDateString.isEmpty() || synopsis.isEmpty()
        ) {
            Toast.makeText(this, "Preencha todos os campos!", Toast.LENGTH_SHORT).show()
            return
        }

        if (!isValidMovieName(movieName)) {
            Toast.makeText(this, "Nome do filme inválido!", Toast.LENGTH_SHORT).show()
            return
        }

        if (!isValidName(directorName)) {
            Toast.makeText(this, "Nome do diretor inválido!", Toast.LENGTH_SHORT).show()
            return
        }

        var ageMin = 0
        ageMin = try {
            ageMinString.toInt()
        } catch (e: NumberFormatException) {
            Log.e("AddMovieActivity", "Error parsing age: " + e.message)
            Toast.makeText(this, "Idade mínima inválida!", Toast.LENGTH_SHORT).show()
            return
        }

        if (ageMin < 0) {
            Toast.makeText(this, "Idade mínima deve ser não negativa!", Toast.LENGTH_SHORT).show()
            return
        }

        if (!isValidDateFormat(releaseDateString)) {
            Toast.makeText(this, "Data de lançamento inválida!", Toast.LENGTH_SHORT).show()
            return
        }

        val movieData = HashMap<String, Any>()
        movieData["name"] = movieName
        movieData["director"] = directorName
        movieData["ageMin"] = ageMin
        movieData["releaseDate"] = releaseDateString
        movieData["synopsis"] = synopsis

        val moviesRef = db!!.collection("movies")

        moviesRef.add(movieData)
            .addOnSuccessListener { documentReference: DocumentReference? ->
                Toast.makeText(
                    this@AddMovieActivity,
                    "Filme cadastrado com sucesso!",
                    Toast.LENGTH_SHORT
                ).show()
            }
            .addOnFailureListener { e: Exception? ->
                Log.w("AddMovieActivity", "Error adding movie to Firestore", e)
                Toast.makeText(
                    this@AddMovieActivity,
                    "Erro ao cadastrar filme!",
                    Toast.LENGTH_SHORT
                )
                    .show()
            }
    }
}
