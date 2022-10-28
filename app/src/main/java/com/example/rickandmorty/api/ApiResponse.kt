package com.example.rickandmorty.api

import com.example.rickandmorty.data.Character

data class ApiResponse(
    val results: List<Character>
)
