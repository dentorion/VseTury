package com.entin.data.remote

import com.entin.data.model.ArticleFirebaseModel
import com.entin.data.model.TourFirebaseModel
import com.entin.presentation.model.LogOpenScreen
import com.entin.presentation.model.SliderItemModel
import kotlinx.coroutines.flow.Flow

interface FirebaseRepository {

    suspend fun searchTours(
        cityFromId: Int,
        cityToId: Int,
        dateFrom: Long,
        dateTo: Long,
    ): Flow<Result<List<TourFirebaseModel>>>

    suspend fun logSearch(
        cities: Pair<Int, Int>,
        dates: Pair<Long, Long>
    )

    suspend fun logOpenTour(logOpenScreen: LogOpenScreen)

    suspend fun getSliderItems(): Flow<List<SliderItemModel>>

    suspend fun getTourById(tourId: Int): Flow<TourFirebaseModel>

    suspend fun searchToursToCity(cityId: Int): Flow<List<TourFirebaseModel>>

    suspend fun searchToursFromCity(cityId: Int): Flow<List<TourFirebaseModel>>

    suspend fun soonTours(limitTours: Int): Flow<List<TourFirebaseModel>>

    suspend fun getArticles(): Flow<List<ArticleFirebaseModel>>

    suspend fun getArticleById(articleId: Int): Flow<ArticleFirebaseModel>
}