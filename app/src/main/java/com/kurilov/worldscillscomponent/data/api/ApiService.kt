package com.kurilov.worldscillscomponent.data.api

import com.kurilov.worldscillscomponent.data.classes.Movie
import retrofit2.http.GET

interface ApiService {

    @GET("v3.1/all/")
    suspend fun getMovies() : List<Movie>
}