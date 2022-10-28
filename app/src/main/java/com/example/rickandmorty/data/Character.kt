package com.example.rickandmorty.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Character(
    val gender: String,
    val id: Int,
    val name: String,
    val origin: Origin,
    val species: String,
    val status: String,
    val type: String,
    val image: String
):Parcelable