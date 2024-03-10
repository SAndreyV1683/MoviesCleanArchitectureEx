package com.example.moviescleanarchitectureex.presentation.poster

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.moviescleanarchitectureex.MoviesCastActivity
import com.example.moviescleanarchitectureex.appComponent
import com.example.moviescleanarchitectureex.databinding.FragmentAboutBinding
import com.example.moviescleanarchitectureex.domen.models.MovieDetails
import com.example.moviescleanarchitectureex.ui.models.AboutState
import javax.inject.Inject

class AboutFragment: Fragment() {

    lateinit var movieId: String
    private val aboutViewModel: AboutViewModel by viewModels {
        factory.create(movieId)
    }
    @Inject
    lateinit var factory: AboutViewModel.AboutViewModelFactory.Factory

    private lateinit var binding: FragmentAboutBinding

    override fun onAttach(context: Context) {
        context.appComponent.inject(this)
        movieId  = requireArguments().getString(MOVIE_ID, "")
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAboutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        aboutViewModel.observeState().observe(viewLifecycleOwner) {
            when (it) {
                is AboutState.Content -> showDetails(it.movie)
                is AboutState.Error -> showErrorMessage(it.message)
            }
        }
        binding.showCastButton.setOnClickListener {
            startActivity(
                MoviesCastActivity.newInstance(
                    context = requireContext(),
                    movieId = requireArguments().getString(MOVIE_ID).orEmpty()
                )
            )
        }
    }

    private fun showErrorMessage(message: String) {
        binding.apply {
            details.visibility = View.GONE
            errorMessage.visibility = View.VISIBLE
            errorMessage.text = message
        }
    }

    private fun showDetails(movieDetails: MovieDetails) {
        binding.apply {
            details.visibility = View.VISIBLE
            errorMessage.visibility = View.GONE
            title.text = movieDetails.title
            ratingValue.text = movieDetails.imDbRating
            yearValue.text = movieDetails.year
            countryValue.text = movieDetails.countries
            genreValue.text = movieDetails.genres
            directorValue.text = movieDetails.directors
            writerValue.text = movieDetails.writers
            castValue.text = movieDetails.stars
            plot.text = movieDetails.plot
        }
    }

    companion object {
        private const val MOVIE_ID = "movie_id"
        fun newInstance(movieId: String) = AboutFragment().apply { 
            arguments = Bundle().apply { 
                putString(MOVIE_ID, movieId)
            }
        }
    }
}