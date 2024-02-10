package com.example.moviescleanarchitectureex.ui.movies

sealed interface ToastState {
    object None: ToastState
    data class Show(val additionalMessage: String): ToastState
}