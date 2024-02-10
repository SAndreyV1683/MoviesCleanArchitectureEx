package com.example.moviescleanarchitectureex.domen.models

data class Movie(val id: String,
                 val resultType: String,
                 val image: String,
                 val title: String,
                 val description: String,
                 val inFavorite: Boolean
)