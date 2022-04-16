package com.entin.presentation.repository

import com.entin.presentation.model.ArticleDomainModel
import com.entin.presentation.model.LogOpenScreen
import com.entin.presentation.model.SliderItemModel
import com.entin.presentation.model.TourDomainModel
import kotlinx.coroutines.flow.Flow

interface Repository {

    suspend fun logSearch(
        cities: Pair<Int, Int>,
        dates: Pair<Long, Long>
    )

    suspend fun logOpenTour(logOpenScreen: LogOpenScreen)

    suspend fun getSliderItems(): Flow<Result<List<SliderItemModel>>>

    suspend fun searchTours(
        cityFromId: Int,
        cityToId: Int,
        dateFrom: Long,
        dateTo: Long
    ): Flow<Result<List<TourDomainModel>>>

    suspend fun changeFavouriteStateOfTour(tour: TourDomainModel, state: Boolean)

    fun getFavouriteStatusOfTour(
        tourId: Int
    ): Flow<Boolean>

    fun favouriteTours(): Flow<Result<List<TourDomainModel>>>

    suspend fun getUpcomingTours(limitTours: Int): Flow<Result<List<TourDomainModel>>>

    fun getTourById(tourId: Int): Flow<Result<TourDomainModel>>

    suspend fun searchToursFromCity(cityId: Int): Flow<Result<List<TourDomainModel>>>

    suspend fun searchToursToCity(cityId: Int): Flow<Result<List<TourDomainModel>>>

    suspend fun getInfoArticles(): Flow<Result<List<ArticleDomainModel>>>

    suspend fun getArticleById(articleId: Int): Flow<Result<ArticleDomainModel>>
}