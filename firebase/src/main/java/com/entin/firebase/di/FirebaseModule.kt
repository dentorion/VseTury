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
object FirebaseModule {

    /**
     * Tours firebase collection
     */
    @Named(TOURS)
    @Provides
    @Singleton
    fun provideFirebaseTours(): CollectionReference =
        Firebase.firestore.collection(TOURS)

    /**
     * Logging search queries firebase collection
     */
    @Named(LOGS_SEARCH)
    @Provides
    @Singleton
    fun provideFirebaseLogsSearch(): CollectionReference =
        Firebase.firestore.collection(LOGS_SEARCH)

    /**
     * Logging opened screen firebase collection
     */
    @Named(LOGS_OPEN_SCREEN)
    @Provides
    @Singleton
    fun provideFirebaseLogsOpenScreen(): CollectionReference =
        Firebase.firestore.collection(LOGS_OPEN_SCREEN)

    /**
     * Slider firebase collection
     */
    @Named(SLIDER)
    @Provides
    @Singleton
    fun provideFirebaseSlider(): CollectionReference =
        Firebase.firestore.collection(SLIDER)

    /**
     * Articles for info screen
     */
    @Named(ARTICLES)
    @Provides
    @Singleton
    fun provideFirebaseArticles(): CollectionReference =
        Firebase.firestore.collection(ARTICLES)

    const val TOURS = "tours"
    const val LOGS_SEARCH = "logs_search"
    const val LOGS_OPEN_SCREEN = "logs_open_screen"
    const val SLIDER = "slider"
    const val ARTICLES = "articles"
}
