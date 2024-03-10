package com.example.moviescleanarchitectureex.presentation.moviescast

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.moviescleanarchitectureex.domen.models.MovieCastPerson

class MoviesCastAdapter: RecyclerView.Adapter<MovieCastViewHolder>() {

    var persons = emptyList<MovieCastPerson>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieCastViewHolder = MovieCastViewHolder(parent)

    override fun getItemCount(): Int = persons.size

    override fun onBindViewHolder(holder: MovieCastViewHolder, position: Int) {
        holder.bind(persons[position])
    }
}