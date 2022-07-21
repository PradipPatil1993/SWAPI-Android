package com.example.startwars.api.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Character (
    val count: Long = 0,
    val next: String?= null,
    val previous: String? = null,
    val results: List<Result>
) : Parcelable

@Parcelize
data class Result (
    val name: String,
    val height: String? = null,
    val birthYear: String? = null,
    val films: List<String>? = null
) : Parcelable

