package com.example.moviescleanarchitectureex.ui.root

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.example.moviescleanarchitectureex.R
import com.example.moviescleanarchitectureex.databinding.ActivityRootBinding
import com.example.moviescleanarchitectureex.ui.movies.MoviesFragment

class RootActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRootBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRootBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }


}