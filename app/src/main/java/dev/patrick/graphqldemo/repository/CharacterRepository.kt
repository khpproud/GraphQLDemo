package dev.patrick.graphqldemo.repository

import com.apollographql.apollo.api.Response
import dev.patrick.graphqldemo.CharacterQuery
import dev.patrick.graphqldemo.CharactersListQuery

interface CharacterRepository {

    suspend fun queryCharactersList(): Response<CharactersListQuery.Data>

    suspend fun queryCharacter(id: String): Response<CharacterQuery.Data>
}