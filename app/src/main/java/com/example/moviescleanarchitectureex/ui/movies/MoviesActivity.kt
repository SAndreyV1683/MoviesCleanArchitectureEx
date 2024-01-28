package com.example.moviescleanarchitectureex.ui.movies

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.moviescleanarchitectureex.util.Creator
import com.example.moviescleanarchitectureex.R
import com.example.moviescleanarchitectureex.domen.models.Movie
import com.example.moviescleanarchitectureex.presentation.movies.MoviesView
import com.example.moviescleanarchitectureex.ui.poster.PosterActivity

class MoviesActivity : AppCompatActivity(), MoviesView {

    private val handler = Handler(Looper.getMainLooper())
    private lateinit var queryInput: EditText
    private lateinit var placeholderMessage: TextView
    private lateinit var moviesList: RecyclerView
    private lateinit var progressBar: ProgressBar
    private var textWatcher: TextWatcher? = null

    companion object {
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }
    private val adapter = MoviesAdapter {
        if (clickDebounce()) {
            val intent = Intent(this, PosterActivity::class.java)
            intent.putExtra("poster", it.image)
            startActivity(intent)
        }
    }

    private var isClickAllowed = true

    private val moviesSearchPresenter = Creator.provideMoviesSearchPresenter(moviesView = this, context = this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movies)


        placeholderMessage = findViewById(R.id.placeholderMessage)
        queryInput = findViewById(R.id.queryInput)
        moviesList = findViewById(R.id.locations)
        progressBar = findViewById(R.id.progressBar)

        moviesList.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        moviesList.adapter = adapter
        textWatcher = object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                moviesSearchPresenter.searchDebounce(
                    changedText = queryInput.text.toString()
                )
            }

            override fun afterTextChanged(p0: Editable?) {
            }

        }
        queryInput.addTextChangedListener(textWatcher)

    }


    override fun onDestroy() {
        super.onDestroy()
        moviesSearchPresenter.onDestroy()
        textWatcher?.let { queryInput.removeTextChangedListener(it) }
    }

    private fun clickDebounce() : Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            handler.postDelayed({ isClickAllowed = true }, CLICK_DEBOUNCE_DELAY)
        }
        return current
    }

    override fun showPlaceholderMessage(isVisible: Boolean) {
        placeholderMessage.visibility = if (isVisible) View.VISIBLE else View.GONE
    }

    override fun showMoviesList(isVisible: Boolean) {
        moviesList.visibility = if (isVisible) View.VISIBLE else View.GONE
    }

    override fun showProgressBar(isVisible: Boolean) {
        progressBar.visibility = if (isVisible) View.VISIBLE else View.GONE
    }

    override fun changePlaceholderText(newPlaceholderText: String) {
        placeholderMessage.text = newPlaceholderText
    }

    override fun updateMoviesList(newMoviesList: List<Movie>) {
        adapter.movies.clear()
        adapter.movies.addAll(newMoviesList)
        adapter.notifyDataSetChanged()
    }

    override fun showMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG)
            .show()
    }


}