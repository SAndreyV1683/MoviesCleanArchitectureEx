package com.example.moviescleanarchitectureex.presentation.poster

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.moviescleanarchitectureex.MoviesApplication
import com.example.moviescleanarchitectureex.databinding.FragmentPosterBinding
import javax.inject.Inject

class PosterFragment: Fragment() {

    @Inject
    private lateinit var posterViewModel:PosterViewModel
    private lateinit var binding: FragmentPosterBinding

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
        (requireActivity().application as MoviesApplication).appComponent.inject(this)
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