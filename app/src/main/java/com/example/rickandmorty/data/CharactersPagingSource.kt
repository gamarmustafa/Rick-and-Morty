package com.example.rickandmorty.data

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.rickandmorty.api.Api
import retrofit2.HttpException
import java.io.IOException

private const val STARTING_PAGE_INDEX = 1

class CharactersPagingSource(private val api: Api,private val name:String,private val status:String,private val gender:String ) :
    PagingSource<Int, Character>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Character> {
        val position = params.key ?: STARTING_PAGE_INDEX
        return try {
            val nextPage: Int = params.key ?: STARTING_PAGE_INDEX
            val response = api.getCharacters(nextPage,name,status,gender)
            val characters = response.results

            LoadResult.Page(
                data = characters,
                prevKey = if (position == STARTING_PAGE_INDEX) null else position - 1,
                nextKey = if (characters.isEmpty()) null else position + 1
            )
        } catch (exception: IOException) {
            LoadResult.Error(exception)
        } catch (exception: HttpException) {
            LoadResult.Error(exception)
        }

    }

    override fun getRefreshKey(state: PagingState<Int, Character>): Int? {
        TODO("Not yet implemented")
    }
}