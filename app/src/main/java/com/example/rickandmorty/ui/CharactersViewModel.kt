package com.example.rickandmorty.ui
import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.rickandmorty.data.Character
import com.example.rickandmorty.data.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import javax.inject.Inject

@HiltViewModel
class CharactersViewModel @Inject constructor(private val repository: Repository):ViewModel() {

    private val currentQuery = MutableStateFlow(DEFAULT_NAME_QUERY)
    private val currentStatus = MutableStateFlow(DEFAULT_STATUS)
    private val currentGender = MutableStateFlow(DEFAULT_GENDER)


    private val charactersFlow =
        combine(currentQuery, currentStatus, currentGender) { name, status, gender ->
            Triple(name, status, gender)

        }.flatMapLatest {
            repository.getCharacters(
                currentQuery.value,
                currentStatus.value,
                currentGender.value
            ).cachedIn(viewModelScope)
        }

    val characters = charactersFlow.asLiveData()

    val refreshedCharacters = repository.getCharacters("","","").cachedIn(viewModelScope).asLiveData()

    fun searchCharacterByName(query: String) {
        currentQuery.value = query
    }

    fun searchCharacterByStatus(status: String) {
        currentStatus.value = status
    }

    fun searchCharacterByGender(gender: String) {
        currentGender.value = gender
    }

    companion object{
        private const val DEFAULT_NAME_QUERY = ""
        private const val DEFAULT_STATUS = ""
        private const val DEFAULT_GENDER = ""
    }

}