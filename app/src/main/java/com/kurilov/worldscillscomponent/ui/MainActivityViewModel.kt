package com.kurilov.worldscillscomponent.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kurilov.worldscillscomponent.data.repositories.ApiRepo
import kotlinx.coroutines.launch

class MainActivityViewModel : ViewModel() {

    private val apiRepo = ApiRepo

    val movies = apiRepo.movies

    fun loadigMovies() {
        viewModelScope.launch {
            apiRepo.getMovies()
        }
    }
}