package com.example.moviescleanarchitectureex.presentation.poster

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.moviescleanarchitectureex.MoviesApplication
import com.example.moviescleanarchitectureex.appComponent
import com.example.moviescleanarchitectureex.databinding.FragmentPosterBinding
import javax.inject.Inject

class PosterFragment: Fragment() {

    private val posterViewModel: PosterViewModel by viewModels {
        factory.create(posterUrl)
    }
    @Inject
    lateinit var factory: PosterViewModel.PosterViewModelFactory.Factory
    private lateinit var posterUrl: String

    private lateinit var binding: FragmentPosterBinding

    override fun onAttach(context: Context) {
        posterUrl = requireArguments().getString(POSTER_URL, "")
        context.appComponent.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPosterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        MoviesApplication.INSTANCE.appComponent.inject(this)
        posterViewModel.observeUrl().observe(viewLifecycleOwner) {
            showPoster(it)
        }
    }

    private fun showPoster(url: String?) {
        context?.let {
            Glide.with(it)
                .load(url)
                .into(binding.poster)
        }
    }

    companion object {
        private const val POSTER_URL = "poster_url"
        fun newInstance(posterUrl: String) = PosterFragment().apply {
            arguments = Bundle().apply {
                putString(POSTER_URL, posterUrl)
            }
        }
    }
}