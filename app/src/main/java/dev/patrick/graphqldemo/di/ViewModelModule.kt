package dev.patrick.graphqldemo.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import dev.patrick.graphqldemo.repository.CharacterRepository
import dev.patrick.graphqldemo.repository.CharacterRepositoryImpl

@Module
@InstallIn(ViewModelComponent::class)
interface ViewModelModule {

    @Binds
    @ViewModelScoped
    fun bindRepository(repo: CharacterRepositoryImpl): CharacterRepository
}