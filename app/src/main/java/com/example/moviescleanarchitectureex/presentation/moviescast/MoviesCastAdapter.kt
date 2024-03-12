package com.example.moviescleanarchitectureex.presentation.moviescast

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.moviescleanarchitectureex.R
import com.example.moviescleanarchitectureex.domen.models.MovieCastPerson

class MoviesCastAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var items = emptyList<MoviesCastRVItem>()

    override fun getItemCount(): Int = items.size

    override fun getItemViewType(position: Int): Int {
        return when (items[position]) {
            is MoviesCastRVItem.HeaderItem -> R.layout.list_item_header
            is MoviesCastRVItem.PersonItem -> R.layout.list_item_cast
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder = when (viewType) {
        R.layout.list_item_header -> MovieCastHeaderViewHolder(parent)
        R.layout.list_item_cast -> MovieCastViewHolder(parent)
        else -> error("Unknown viewType create [$viewType]")
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder.itemViewType) {
            R.layout.list_item_header -> {
                val headerHolder = holder as MovieCastHeaderViewHolder
                headerHolder.bind(items[position] as MoviesCastRVItem.HeaderItem)
            }

            R.layout.list_item_cast -> {
                val headerHolder = holder as MovieCastViewHolder
                headerHolder.bind(items[position] as MoviesCastRVItem.PersonItem)
            }

            else -> error("Unknown viewType bind [${holder.itemViewType}]")
        }
    }

}