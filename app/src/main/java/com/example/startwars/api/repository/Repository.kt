package com.example.startwars.api.repository

import com.example.startwars.api.service.ApiService

open class Repository(private val apiService: ApiService) {
    open suspend fun getCharacters(pageNumber: Int = 1) = apiService.getCharacter(pageNumber)
    open suspend fun getCharactersFilms(filmNumber: String) = apiService.getCharacterFilm(filmNumber)
}