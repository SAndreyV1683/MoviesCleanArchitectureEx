package com.example.moviescleanarchitectureex.ui.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.example.moviescleanarchitectureex.R
import com.example.moviescleanarchitectureex.databinding.FragmentDetailsBinding
import com.example.moviescleanarchitectureex.ui.movies.DetailsViewPagerAdapter
import com.google.android.material.tabs.TabLayoutMediator

class DetailsFragment: Fragment() {

    private lateinit var tabMediator: TabLayoutMediator
    private lateinit var binding: FragmentDetailsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val poster = arguments?.getString(ARGS_POSTER_URL) ?: ""
        val movieId = arguments?.getString(ARGS_MOVIE_ID) ?: ""

        binding.viewPager.adapter = DetailsViewPagerAdapter(
            childFragmentManager,
            lifecycle, poster, movieId
        )
        tabMediator = TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            when(position) {
                0 -> tab.text = getString(R.string.poster)
                1 -> tab.text = getString(R.string.details)
            }
        }
        tabMediator.attach()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        tabMediator.detach()
    }

    companion object {
        private const val ARGS_POSTER_URL = "poster_url"
        private const val ARGS_MOVIE_ID = "movie_id"

        fun createArgs(movieId: String, posterUrl: String): Bundle = bundleOf(
            ARGS_MOVIE_ID to movieId,
            ARGS_POSTER_URL to posterUrl
        )
    }

}