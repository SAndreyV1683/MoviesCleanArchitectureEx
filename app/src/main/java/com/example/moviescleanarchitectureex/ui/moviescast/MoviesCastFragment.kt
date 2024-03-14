package com.example.moviescleanarchitectureex.ui.moviescast

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.moviescleanarchitectureex.appComponent
import com.example.moviescleanarchitectureex.databinding.FragmentMoviesCastBinding
import com.example.moviescleanarchitectureex.presentation.moviescast.MoviesCastState
import com.example.moviescleanarchitectureex.presentation.moviescast.MoviesCastViewModel
import com.example.moviescleanarchitectureex.presentation.moviescast.movieCastHeaderDelegate
import com.example.moviescleanarchitectureex.presentation.moviescast.movieCastPersonDelegate
import com.example.moviescleanarchitectureex.ui.root.BindingFragment
import com.hannesdorfmann.adapterdelegates4.ListDelegationAdapter
import javax.inject.Inject

class MoviesCastFragment: BindingFragment<FragmentMoviesCastBinding>() {


    private lateinit var movieId: String
    private val moviesCastViewModel: MoviesCastViewModel by viewModels {
        factory.create(movieId)
    }
    @Inject
    lateinit var factory: MoviesCastViewModel.MovieCastViewModelFactory.Factory
    private val adapter = ListDelegationAdapter(
        movieCastHeaderDelegate(),
        movieCastPersonDelegate()
    )
    override fun onAttach(context: Context) {
        movieId = requireArguments().getString(ARGS_MOVIE_ID) ?: ""
        context.appComponent.inject(this)
        super.onAttach(context)
    }
    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
    ): FragmentMoviesCastBinding {
        return FragmentMoviesCastBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Привязываем адаптер и LayoutManager к RecyclerView
        binding?.movieCastRecyclerView?.adapter = adapter
        binding?.movieCastRecyclerView?.layoutManager = LinearLayoutManager(requireContext())

        // Наблюдаем за UiState из ViewModel
        moviesCastViewModel.observeState().observe(viewLifecycleOwner) { moviesCastState ->
            // В зависимости от UiState экрана показываем
            // разные состояния экрана
            when (moviesCastState) {
                is MoviesCastState.Content -> showContent(moviesCastState)
                is MoviesCastState.Error -> showError(moviesCastState)
                is MoviesCastState.Loading -> showLoading()
            }
        }
    }


    private fun showLoading() {
        binding?.contentContainer?.isVisible = false
        binding?.errorMessageTextView?.isVisible = false

        binding?.progressBar?.isVisible = true
    }

    private fun showError(state: MoviesCastState.Error) {
        binding?.contentContainer?.isVisible = false
        binding?.progressBar?.isVisible = false

        binding?.errorMessageTextView?.isVisible = true
        binding?.errorMessageTextView?.text = state.message
    }

    private fun showContent(state: MoviesCastState.Content) {
        binding?.progressBar?.isVisible = false
        binding?.errorMessageTextView?.isVisible = false

        binding?.contentContainer?.isVisible = true
        binding?.movieTitle?.text = state.fullTitle

        // Просто объединяем всех участников
        // в единый список и отображаем
        adapter.items = state.items
        adapter.notifyDataSetChanged()
    }

    companion object {

        private const val ARGS_MOVIE_ID = "movie_id"

        const val TAG = "MoviesCastFragment"

        fun newInstance(movieId: String): Fragment {
            return MoviesCastFragment().apply {
                arguments = bundleOf(
                    ARGS_MOVIE_ID to movieId
                )
            }
        }
    }
}