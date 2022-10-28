package com.example.rickandmorty.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.liveData
import com.example.rickandmorty.api.Api
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Repository @Inject constructor(private val api:Api) {

    fun getResults() = Pager(
        config = PagingConfig(
            pageSize =20,
            maxSize = 100,
            enablePlaceholders = false
        ),
        pagingSourceFactory = {CharactersPagingSource(api)}
    ).liveData
}