package com.example.moviescleanarchitectureex.ui.movies

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.moviescleanarchitectureex.R
import com.example.moviescleanarchitectureex.databinding.FragmentMoviesBinding
import com.example.moviescleanarchitectureex.domen.models.Movie
import com.example.moviescleanarchitectureex.presentation.movies.MoviesSearchViewModel
import com.example.moviescleanarchitectureex.ui.details.DetailsFragment
import com.example.moviescleanarchitectureex.ui.models.MoviesState
import com.example.moviescleanarchitectureex.ui.root.RootActivity
import com.example.moviescleanarchitectureex.util.debounce
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MoviesFragment: Fragment(){

    companion object {
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }

    private lateinit var binding: FragmentMoviesBinding

    private var adapter: MoviesAdapter? = null

    private lateinit var textWatcher: TextWatcher
    private var isClickAllowed = true
    private lateinit var viewModel: MoviesSearchViewModel
    private lateinit var onMovieClickDebounce: (Movie) -> Unit



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMoviesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = MoviesAdapter(
            object : MoviesAdapter.MovieClickListener {
                override fun onMovieClick(movie: Movie) {
                    (activity as RootActivity).animateBottomNavigationView()
                    onMovieClickDebounce(movie)
                }

                override fun onFavoriteToggleClick(movie: Movie) {
                    viewModel.toggleFavorite(movie)
                }
            }
        )
        viewModel = ViewModelProvider(this, MoviesSearchViewModel.getViewModelFactory())[MoviesSearchViewModel::class.java]
        binding.locations.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.locations.adapter = adapter
        onMovieClickDebounce = debounce(CLICK_DEBOUNCE_DELAY, viewLifecycleOwner.lifecycleScope, false) { movie ->
            findNavController().navigate(
                R.id.action_moviesFragment_to_detailsFragment,
                DetailsFragment.createArgs(movie.id, movie.image)
            )
        }
        textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.searchDebounce(changedText = s?.toString() ?: "")
            }

            override fun afterTextChanged(s: Editable?) {
            }
        }
        textWatcher.let { binding.queryInput.addTextChangedListener(it) }

        viewModel.observeState().observe(viewLifecycleOwner) {
            render(it)
        }

        viewModel.observeShowToast().observe(viewLifecycleOwner) {
            showToast(it)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        adapter = null
        textWatcher.let { binding.queryInput.removeTextChangedListener(it) }
    }
    private fun showToast(additionalMessage: String?) {
        Toast.makeText(requireContext(), additionalMessage, Toast.LENGTH_LONG).show()
    }

    private fun render(state: MoviesState) {
        when (state) {
            is MoviesState.Content -> showContent(state.movies)
            is MoviesState.Empty -> showEmpty(state.message)
            is MoviesState.Error -> showError(state.message)
            is MoviesState.Loading -> showLoading()
        }
    }

    private fun showLoading() {
        binding.locations.visibility = View.GONE
        binding.placeholderMessage.visibility = View.GONE
        binding.progressBar.visibility = View.VISIBLE
    }

    private fun showError(errorMessage: String) {
        binding.locations.visibility = View.GONE
        binding.placeholderMessage.visibility = View.VISIBLE
        binding.progressBar.visibility = View.GONE
        binding.placeholderMessage.text = errorMessage
    }

    private fun showEmpty(emptyMessage: String) {
        showError(emptyMessage)
    }

    private fun showContent(movies: List<Movie>) {
        binding.locations.visibility = View.VISIBLE
        binding.placeholderMessage.visibility = View.GONE
        binding.progressBar.visibility = View.GONE

        adapter?.movies?.clear()
        adapter?.movies?.addAll(movies)
        adapter?.notifyDataSetChanged()
    }

    private fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            viewLifecycleOwner.lifecycleScope.launch {
                delay(CLICK_DEBOUNCE_DELAY)
                isClickAllowed = true
            }
        }
        return current
    }
}