package com.entin.core.di

import com.entin.data.remote.FirebaseRepository
import com.entin.data.repository.RepositoryImpl
import com.entin.presentation.repository.Repository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RepositoriesModule {

    @Singleton
    @Provides
    fun provideRepository(firebaseRepository: FirebaseRepository): Repository {
        return RepositoryImpl(firebaseRepository)
    }
}