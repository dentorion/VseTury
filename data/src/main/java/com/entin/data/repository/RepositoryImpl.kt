package com.entin.data.repository

import com.entin.data.extension.*
import com.entin.data.local.Cities
import com.entin.data.remote.FirebaseRepository
import com.entin.presentation.model.*
import com.entin.presentation.repository.Repository
import com.entin.room.db.dao.ToursDAO
import com.entin.room.db.entity.FavouriteTourRoomModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

/**
 * Implementation of [Repository] interface
 */

class RepositoryImpl @Inject constructor(
    private val firebaseRepository: FirebaseRepository,
    private val cities: Cities,
    private val toursDAO: ToursDAO,
) : Repository {

    /**
     * Log search user query
     */
    override suspend fun logSearch(cities: Pair<Int, Int>, dates: Pair<Long, Long>) =
        firebaseRepository.logSearch(cities, dates)

    /**
     * Log open screen
     */
    override suspend fun logOpenTour(logOpenScreen: LogOpenScreen) =
        firebaseRepository.logOpenTour(logOpenScreen)

    /**
     * Get items for slider on Main Screen
     */
    override suspend fun getSliderItems(): Flow<Result<List<SliderItemModel>>> = flow {
        firebaseRepository.getSliderItems().collect { listSlider ->
            emit(Result.success(listSlider))
        }
    }

    /**
     * Get list of tours by searching from city to city with limit by date
     */
    override suspend fun searchTours(
        cityFromId: Int,
        cityToId: Int,
        dateFrom: Long,
        dateTo: Long
    ): Flow<Result<List<TourDomainModel>>> = flow {
        try {
            firebaseRepository.searchTours(cityFromId, cityToId, dateFrom, dateTo)
                .collect { result ->
                    result.onSuccess { tourListFirebaseModel ->
                        /** Convert list of TourFirebaseModel to list of TourDomainModel */
                        emit(
                            Result.success(
                                convertListTourFirebaseModelToListTourDomainModel(
                                    ::getCityById,
                                    tourListFirebaseModel
                                )
                            )
                        )
                    }
                }
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }

    /**
     * Change favourite status of tour
     */
    override suspend fun changeFavouriteStateOfTour(
        tour: TourDomainModel,
        state: Boolean,
    ) {
        if (state) {
            toursDAO.delFavouriteTour(tour.id)
        } else {
            toursDAO.addFavouriteTour(
                FavouriteTourRoomModel(
                    favouriteTourId = tour.id,
                    favouriteStatus = state.not(),
                    favouriteTourCitiesFrom = tour.citiesFrom,
                    favouriteTourCitiesFromName = tour.citiesFromName,
                    favouriteTourCityToName = tour.cityToName,
                    favouriteTourCityToId = tour.cityToId,
                    favouriteTourDateFrom = tour.dateFrom,
                    favouriteTourDateTo = tour.dateTo,
                    favouriteTourTitle = tour.title,
                )
            )
        }
    }

    /**
     * Get favourite state of tour
     */
    override fun getFavouriteStatusOfTour(tourId: Int): Flow<Boolean> = flow {
        toursDAO.getFavouriteStatusOfTour(tourId).collect { tour ->
            if (tour != null) {
                emit(tour.favouriteStatus)
            } else {
                emit(false)
            }
        }
    }

    /**
     * Get user favourite list of tours
     */
    override fun favouriteTours(): Flow<Result<List<TourDomainModel>>> = flow {
        toursDAO.getAllFavouriteTours().collect {
            emit(
                Result.success(
                    /** Convert list of FavouriteTourRoomModel to list of TourDomainModel */
                    convertListFavouriteTourRoomModelToListDomainTourModel(it)
                )
            )
        }
    }

    /**
     * Get upcoming tours limited by quantity
     */
    override suspend fun getUpcomingTours(limitTours: Int): Flow<Result<List<TourDomainModel>>> =
        flow {
            firebaseRepository.soonTours(limitTours).collect { tourListFirebaseModel ->
                emit(
                    /** Convert into [TourDomainModel] */
                    Result.success(
                        convertListTourFirebaseModelToListTourDomainModel(
                            ::getCityById,
                            tourListFirebaseModel
                        )
                    )
                )
            }
        }

    /**
     * Get tour by Id
     */
    override fun getTourById(tourId: Int): Flow<Result<TourDomainModel>> = flow {
        firebaseRepository.getTourById(tourId).collect { tourFirebaseModel ->
            emit(
                /** Convert into [TourDomainModel] */
                Result.success(tourFirebaseModel.convertToTourDomainModel(::getCityById))
            )
        }
    }

    /**
     * Get tours from city by Id
     */
    override suspend fun searchToursFromCity(cityId: Int): Flow<Result<List<TourDomainModel>>> =
        flow {
            firebaseRepository.searchToursFromCity(cityId).collect { tourListFirebaseModel ->
                emit(
                    /** Convert into [TourDomainModel] */
                    Result.success(
                        convertListTourFirebaseModelToListTourDomainModel(
                            ::getCityById,
                            tourListFirebaseModel
                        )
                    )
                )
            }
        }


    /**
     * Get tours to city by Id
     */
    override suspend fun searchToursToCity(cityId: Int): Flow<Result<List<TourDomainModel>>> =
        flow {
            firebaseRepository.searchToursToCity(cityId).collect { tourListFirebaseModel ->
                emit(
                    /** Convert into [TourDomainModel] */
                    Result.success(
                        convertListTourFirebaseModelToListTourDomainModel(
                            ::getCityById,
                            tourListFirebaseModel
                        )
                    )
                )
            }
        }

    /**
     * Get list of articles
     */
    override suspend fun getInfoArticles(): Flow<Result<List<ArticleDomainModel>>> =
        flow {
            firebaseRepository.getArticles().collect { articlesList ->
                emit(
                    /** Convert into [ArticleDomainModel] */
                    Result.success(
                        convertListArticleFirebaseModelToListArticleDomainModel(articlesList)
                    )
                )
            }
        }

    /**
     * Get article by Id
     */
    override suspend fun getArticleById(articleId: Int): Flow<Result<ArticleDomainModel>> =
        flow {
            firebaseRepository.getArticleById(articleId).collect { article ->
                emit(Result.success(article.convertToArticleDomainModel()))
            }
        }

    /**
     * Get [CityDomainModel] by it's id
     */
    private fun getCityById(value: Int): CityDomainModel? =
        cities.getCityById(value)
}