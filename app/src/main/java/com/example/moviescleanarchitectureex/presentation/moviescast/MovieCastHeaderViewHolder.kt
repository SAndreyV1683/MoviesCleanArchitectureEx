package com.example.moviescleanarchitectureex.presentation.moviescast

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.moviescleanarchitectureex.R
import com.example.moviescleanarchitectureex.databinding.ListItemHeaderBinding

class MovieCastHeaderViewHolder(parent: ViewGroup): RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context).inflate(R.layout.list_item_header, parent, false)
) {

    private val binding: ListItemHeaderBinding = ListItemHeaderBinding.bind(itemView)
    fun bind(item : MoviesCastRVItem.HeaderItem) {
        binding.headerTextView.text = item.headerText
    }
}