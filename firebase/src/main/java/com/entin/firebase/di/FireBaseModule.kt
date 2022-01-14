package com.entin.firebase.di

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton

/**
 * Providing Firebase collections, they are lightweight
 */

@Module
@InstallIn(SingletonComponent::class)
object FireBaseModule {

    /**
     * Concerts firebase collection
     */
    @Named(TOURS)
    @Provides
    @Singleton
    fun provideFirebaseTours(): CollectionReference =
        Firebase.firestore.collection(TOURS)

    /**
     * Errors collection
     */
    @Named(LOGS)
    @Provides
    @Singleton
    fun provideFirebaseLogs(): CollectionReference =
        Firebase.firestore.collection(LOGS)

    const val TOURS = "tours"
    const val LOGS = "logs"
}
