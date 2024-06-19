import android.content.Context
import android.content.SharedPreferences
import com.example.filmoteca.Movie
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object PreferenceManager {
    private const val PREFERENCE_NAME = "filmoteca_preferences"
    private const val KEY_FAVORITE_MOVIES = "favorite_movies"
    private const val KEY_WATCH_LATER_MOVIES = "watch_later_movies"

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

    fun getWatchLaterMovies(context: Context): MutableList<Movie> {
        val sharedPreferences = getSharedPreferences(context)
        val gson = Gson()
        val json = sharedPreferences.getString(KEY_WATCH_LATER_MOVIES, null)
        val type = object : TypeToken<MutableList<Movie>>() {}.type
        return gson.fromJson(json, type) ?: mutableListOf()
    }

    fun addFavoriteMovie(context: Context, movie: Movie) {
        val sharedPreferences = getSharedPreferences(context)
        val editor = sharedPreferences.edit()
        val gson = Gson()

        val favoriteMovies = getFavoriteMovies(context)

        if (!favoriteMovies.contains(movie)) {
            favoriteMovies.add(movie)
        }

        val json = gson.toJson(favoriteMovies)
        editor.putString(KEY_FAVORITE_MOVIES, json)
        editor.apply()
    }

    fun addWatchLaterMovies(context: Context, movie: Movie) {
        val sharedPreferences = getSharedPreferences(context)
        val editor = sharedPreferences.edit()
        val gson = Gson()

        val watchLaterMovies = getWatchLaterMovies(context)

        if (!watchLaterMovies.contains(movie)) {
            watchLaterMovies.add(movie)
        }

        val json = gson.toJson(watchLaterMovies)
        editor.putString(KEY_WATCH_LATER_MOVIES, json)
        editor.apply()
    }

    fun removeFavoriteMovie(context: Context, movie: Movie) {
        val sharedPreferences = getSharedPreferences(context)
        val editor = sharedPreferences.edit()
        val gson = Gson()

        val favoriteMovies = getFavoriteMovies(context)

        favoriteMovies.remove(movie)

        val json = gson.toJson(favoriteMovies)
        editor.putString(KEY_FAVORITE_MOVIES, json)
        editor.apply()
    }
}
