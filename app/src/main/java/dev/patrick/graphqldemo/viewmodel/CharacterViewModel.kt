package dev.patrick.graphqldemo.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.exception.ApolloException
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.patrick.graphqldemo.CharacterQuery
import dev.patrick.graphqldemo.CharactersListQuery
import dev.patrick.graphqldemo.repository.CharacterRepository
import dev.patrick.graphqldemo.view.state.ViewState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import javax.inject.Inject

@ExperimentalCoroutinesApi
@HiltViewModel
class CharacterViewModel @Inject constructor(
    private val repository: CharacterRepository
) : ViewModel() {

    private val _charactersList by lazy { MutableLiveData<ViewState<Response<CharactersListQuery.Data>>>() }
    val charactersList: LiveData<ViewState<Response<CharactersListQuery.Data>>>
        get() = _charactersList

    private val _character by lazy { MutableLiveData<ViewState<Response<CharacterQuery.Data>>>() }
    val character: LiveData<ViewState<Response<CharacterQuery.Data>>>
        get() = _character

    fun queryCharactersList() = viewModelScope.launch {
        _charactersList.value = ViewState.Loading
        try {
            val response = repository.queryCharactersList()
            _charactersList.value = ViewState.Success(response)
        } catch (e: ApolloException) {
            _charactersList.value = ViewState.Error("Error fetching characters")
        }
    }

    fun queryCharacter(id: String) = viewModelScope.launch {
        _character.value = ViewState.Loading
        try {
            val response = repository.queryCharacter(id)
            _character.value = ViewState.Success(response)
        } catch (e: ApolloException) {
            _character.value = ViewState.Error("Error fetching characters")
        }
    }
}