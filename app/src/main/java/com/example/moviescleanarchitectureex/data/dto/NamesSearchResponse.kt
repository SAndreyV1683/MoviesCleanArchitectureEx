package com.example.moviescleanarchitectureex.data.dto

class NamesSearchResponse(val searchType: String,
                          val expression: String,
                          val results: List<PersonDto>) : Response()