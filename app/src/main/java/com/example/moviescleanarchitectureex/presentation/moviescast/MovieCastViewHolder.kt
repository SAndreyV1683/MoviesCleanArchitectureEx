package com.example.moviescleanarchitectureex.presentation.moviescast

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.moviescleanarchitectureex.R
import com.example.moviescleanarchitectureex.databinding.ListItemCastBinding
import com.example.moviescleanarchitectureex.domen.models.MovieCastPerson

class MovieCastViewHolder(parent: ViewGroup) :
    RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.list_item_cast, parent, false)
    ) {

        private val binding = ListItemCastBinding.bind(itemView)
        fun bind(movieCastPerson: MovieCastPerson) {
            if (movieCastPerson.image == null) {
                binding.actorImageView.isVisible = false
            } else {
                Glide.with(itemView)
                    .load(movieCastPerson.image)
                    .into(binding.actorImageView)
                binding.actorImageView.isVisible = true
            }

            binding.actorNameTextView.text = movieCastPerson.name
            binding.actorDescriptionTextView.text = movieCastPerson.description
        }
}