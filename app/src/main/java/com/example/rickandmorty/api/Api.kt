package com.example.rickandmorty.api

import retrofit2.http.GET
import retrofit2.http.Query

interface Api {

    companion object{
       const val BASE_URL ="https://rickandmortyapi.com/api/"
    }

    @GET("character")
    suspend fun getCharacters(@Query("page") query: Int):ApiResponse

}