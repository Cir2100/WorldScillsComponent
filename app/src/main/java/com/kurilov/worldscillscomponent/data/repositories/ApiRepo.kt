package com.kurilov.worldscillscomponent.data.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.kurilov.worldscillscomponent.data.api.RetrofitBuilder
import com.kurilov.worldscillscomponent.data.classes.MResult
import com.kurilov.worldscillscomponent.data.classes.Movie

object ApiRepo {

    private val apiService = RetrofitBuilder.apiService

    private val _movies = MutableLiveData<MResult<List<Movie>>>()
    val movies : LiveData<MResult<List<Movie>>>
        get() = _movies

    suspend fun getMovies() {
        _movies.postValue(MResult.loading(data = null))
        try {
            _movies.postValue(MResult.success(data = apiService.getMovies()))
        }catch (e : Exception) {
            _movies.postValue(MResult.error(data = null, message = e.message ?: "Error Occurred!" ))
        }
    }
}