package com.entin.firebase.di

import com.entin.data.remote.FirebaseRepository
import com.entin.firebase.repository.FirebaseRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface FirebaseRepositoryModule {

    @Binds
    fun bindAnalyticsService(
        firebaseRepositoryImpl: FirebaseRepositoryImpl
    ): FirebaseRepository
}