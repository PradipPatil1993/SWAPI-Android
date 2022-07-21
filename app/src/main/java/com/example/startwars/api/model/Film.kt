package com.example.startwars.api.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Film (
    val title: String,
    val episodeID: Long,
    val opening_crawl: String,
    val director: String,
    val producer: String,
    val releaseDate: String,
    val characters: List<String>,
    val planets: List<String>,
    val starships: List<String>,
    val vehicles: List<String>,
    val species: List<String>,
    val created: String,
    val edited: String,
    val url: String
) : Parcelable
