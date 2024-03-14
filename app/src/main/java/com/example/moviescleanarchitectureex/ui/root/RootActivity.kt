package com.example.moviescleanarchitectureex.ui.root

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.moviescleanarchitectureex.R
import com.example.moviescleanarchitectureex.databinding.ActivityRootBinding
import com.example.moviescleanarchitectureex.ui.movies.MoviesFragment

class RootActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRootBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRootBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .setReorderingAllowed(true)
                .add(R.id.rootFragmentContainerView, MoviesFragment())
                .commit()
        }
    }
}