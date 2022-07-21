package com.example.startwars.api.service

import com.example.startwars.api.model.Character
import com.example.startwars.api.model.Film
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("people")
    suspend fun getCharacter(@Query("page") pageNumber: Int): Response<Character>

    @GET("films/{id}")
    suspend fun getCharacterFilm(@Path("id") filmId: String): Response<Film>
}