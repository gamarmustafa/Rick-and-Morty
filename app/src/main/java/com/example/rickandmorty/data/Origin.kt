package com.example.rickandmorty.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Origin(
    val name: String,
    val url: String
):Parcelable