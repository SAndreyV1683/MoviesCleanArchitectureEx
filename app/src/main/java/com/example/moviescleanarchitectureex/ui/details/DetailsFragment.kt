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
import com.example.moviescleanarchitectureex.ui.root.BindingFragment
import com.google.android.material.tabs.TabLayoutMediator

class DetailsFragment: BindingFragment<FragmentDetailsBinding>() {

    private lateinit var tabMediator: TabLayoutMediator
    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
    ): FragmentDetailsBinding {
        return FragmentDetailsBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val poster = arguments?.getString("poster") ?: ""
        val movieId = arguments?.getString("id") ?: ""

        binding?.viewPager?.adapter = DetailsViewPagerAdapter(childFragmentManager,
            lifecycle, poster, movieId)
        if (binding != null) {
            tabMediator = TabLayoutMediator(binding!!.tabLayout, binding!!.viewPager) { tab, position ->
                when(position) {
                    0 -> tab.text = getString(R.string.poster)
                    1 -> tab.text = getString(R.string.details)
                }
            }
            tabMediator.attach()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        tabMediator.detach()
    }

    companion object {
        private const val POSTER = "poster"
        private const val ID = "id"
        const val TAG = "DetailsFragment"

        fun newInstance(movieId: String, posterUrl: String): Fragment {
            return DetailsFragment().apply {
                arguments = bundleOf(
                    POSTER to posterUrl,
                    ID to movieId
                )
            }
        }
    }

}