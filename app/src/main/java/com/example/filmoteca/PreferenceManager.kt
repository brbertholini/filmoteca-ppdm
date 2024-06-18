import android.content.Context
import android.content.SharedPreferences
import com.example.filmoteca.Movie
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object PreferenceManager {
    private const val PREFERENCE_NAME = "filmoteca_preferences"
    private const val KEY_FAVORITE_MOVIES = "favorite_movies"

    private fun getSharedPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
    }

    fun getFavoriteMovies(context: Context): MutableList<Movie> {
        val sharedPreferences = getSharedPreferences(context)
        val gson = Gson()
        val json = sharedPreferences.getString(KEY_FAVORITE_MOVIES, null)
        val type = object : TypeToken<MutableList<Movie>>() {}.type
        return gson.fromJson(json, type) ?: mutableListOf()
    }

    fun addFavoriteMovie(context: Context, movie: Movie) {
        val sharedPreferences = getSharedPreferences(context)
        val editor = sharedPreferences.edit()
        val gson = Gson()

        // Obter a lista atual de filmes favoritos
        val favoriteMovies = getFavoriteMovies(context)

        // Adicionar o novo filme à lista de filmes favoritos, se ainda não estiver presente
        if (!favoriteMovies.contains(movie)) {
            favoriteMovies.add(movie)
        }

        // Converter a lista para JSON e salvar nas preferências compartilhadas
        val json = gson.toJson(favoriteMovies)
        editor.putString(KEY_FAVORITE_MOVIES, json)
        editor.apply()
    }

    fun removeFavoriteMovie(context: Context, movie: Movie) {
        val sharedPreferences = getSharedPreferences(context)
        val editor = sharedPreferences.edit()
        val gson = Gson()

        // Obter a lista atual de filmes favoritos
        val favoriteMovies = getFavoriteMovies(context)

        // Remover o filme da lista de filmes favoritos
        favoriteMovies.remove(movie)

        // Converter a lista para JSON e salvar nas preferências compartilhadas
        val json = gson.toJson(favoriteMovies)
        editor.putString(KEY_FAVORITE_MOVIES, json)
        editor.apply()
    }
}
