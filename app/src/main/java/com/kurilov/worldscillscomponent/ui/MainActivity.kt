package com.kurilov.worldscillscomponent.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.kurilov.worldscillscomponent.databinding.MainActivityBinding
import com.kurilov.worldscillscomponent.component.ListComponentActions
import com.kurilov.worldscillscomponent.data.classes.MStatus


class MainActivity : AppCompatActivity() {

    private lateinit var binding : MainActivityBinding
    private lateinit var viewModel: MainActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = MainActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupViewModel()
        setupUI()
        setupObservers()

        viewModel.loadigMovies()
    }

    private fun setupViewModel() {
        viewModel = ViewModelProvider(this).get(MainActivityViewModel::class.java)
    }

    private fun setupUI() {

        binding.listComponent.radius = 40f

        binding.listComponent.setListener {
            when (it) {
                ListComponentActions.SWIPED_LEFT ->
                    Toast.makeText(this, "Left", Toast.LENGTH_SHORT).show()
                ListComponentActions.SWIPED_RIGHT ->
                    Toast.makeText(this, "Right", Toast.LENGTH_SHORT).show()
                ListComponentActions.URLS_IS_EMPTY ->
                    Toast.makeText(this, "Empty", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupObservers() {
        viewModel.movies.observe(this) {
            it?.let { answer ->
                when(answer.status) {
                    MStatus.SUCCESS -> {
                        binding.progressBar.visibility = View.INVISIBLE
                        answer.data?.let { movies ->
                            val urls = mutableListOf<String>()
                            movies.forEach { movie ->
                                urls.add(movie.flags.png)
                            }
                            binding.listComponent.urls = urls
                        }
                    }

                    MStatus.ERROR -> {
                        binding.progressBar.visibility = View.INVISIBLE
                        Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                    }

                    MStatus.LOADING -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }
                }
            }
        }
    }

}