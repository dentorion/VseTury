package com.entin.firebase.repository

import com.entin.data.model.ArticleFirebaseModel
import com.entin.data.model.TourFirebaseModel
import com.entin.data.remote.FirebaseRepository
import com.entin.firebase.di.FirebaseModule.ARTICLES
import com.entin.firebase.di.FirebaseModule.LOGS_OPEN_SCREEN
import com.entin.firebase.di.FirebaseModule.LOGS_SEARCH
import com.entin.firebase.di.FirebaseModule.SLIDER
import com.entin.firebase.di.FirebaseModule.TOURS
import com.entin.firebase.extension.convertToArticleFirebaseModel
import com.entin.firebase.extension.convertToSliderItem
import com.entin.firebase.extension.convertToTourFirebaseModel
import com.entin.presentation.model.LogOpenScreen
import com.entin.presentation.model.SliderItemModel
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FieldValue
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import java.util.*
import javax.inject.Inject
import javax.inject.Named

class FirebaseRepositoryImpl @Inject constructor(
    @Named(LOGS_SEARCH) private val firebaseDbLogSearch: CollectionReference,
    @Named(LOGS_OPEN_SCREEN) private val firebaseDbLogsOpenScreen: CollectionReference,
    @Named(TOURS) private val firebaseDbTours: CollectionReference,
    @Named(SLIDER) private val firebaseDbSlider: CollectionReference,
    @Named(ARTICLES) private val firebaseInfoArticles: CollectionReference,
) : FirebaseRepository {

    // List of slider items
    private val itemsSlider = mutableListOf<SliderItemModel>()

    // Current day specify in Long (mls) to be sure we search tours with "dateFrom" >= today
    private fun currentDay(): Long = Calendar.getInstance().also {
        it.set(
            Calendar.getInstance().get(Calendar.YEAR),
            Calendar.getInstance().get(Calendar.MONTH),
            Calendar.getInstance().get(Calendar.DAY_OF_MONTH),
            0, 0, 1
        )
    }.time.time

    /**
     * Logging of user actions
     */

    override suspend fun logSearch(cities: Pair<Int, Int>, dates: Pair<Long, Long>) {
        firebaseDbLogSearch.document(Date().time.toString())
            .set(
                mapOf(
                    LOG_CITIES to cities,
                    LOG_DATES to dates,
                    LOG_USER to "null",
                    LOG_INFO to FieldValue.serverTimestamp()
                )
            ).await()
    }

    override suspend fun logOpenTour(logOpenScreen: LogOpenScreen) {
        firebaseDbLogsOpenScreen.document(Date().time.toString())
            .set(
                mapOf(
                    LOG_SCREEN_NAME to logOpenScreen.nameScreen,
                    LOG_SCREEN_PARAMETER to logOpenScreen.parameter,
                    LOG_INFO to FieldValue.serverTimestamp()
                )
            ).await()
    }

    /**
     * Get slider elements for Main screen
     */
    override suspend fun getSliderItems(): Flow<List<SliderItemModel>> = flow {
        itemsSlider.clear()
        firebaseDbSlider.get().await().forEach {
            itemsSlider.add(it.convertToSliderItem())
        }
        if (itemsSlider.isNotEmpty()) {
            emit(itemsSlider)
        }
    }

    /**
     * Common search tours from selected city to selected city
     * that starts after selected date and ends before selected date
     * Q1--T1-------T2--Q2
     */
    override suspend fun searchTours(
        cityFromId: Int,
        cityToId: Int,
        dateFrom: Long,
        dateTo: Long
    ): Flow<Result<List<TourFirebaseModel>>> = flow {
        val resultList = mutableListOf<TourFirebaseModel>()
        /** Find all tours from city (including middle) to city */
        firebaseDbTours
            .whereArrayContains(CITIES_FROM, cityFromId)
            .whereEqualTo(CITY_TO, cityToId)
            .whereGreaterThanOrEqualTo(DATE_FROM, Date(dateFrom))
            .get()
            .await()
            .forEach { document -> resultList.add(document.convertToTourFirebaseModel()) }

        val filteredResult = resultList.filter {
            it.dateTo <= dateTo
        }
        emit(Result.success(filteredResult))
    }

    /**
     * Get Tour by id
     */
    override suspend fun getTourById(tourId: Int): Flow<TourFirebaseModel> = flow {
        firebaseDbTours
            .whereEqualTo(TOUR_ID, tourId)
            .get()
            .await()
            .forEach { document ->
                if (document != null) {
                    emit(document.convertToTourFirebaseModel())
                }
            }
    }

    /**
     * Search tours to city
     */
    override suspend fun searchToursToCity(cityId: Int): Flow<List<TourFirebaseModel>> =
        flow {
            val resultList = mutableListOf<TourFirebaseModel>()
            firebaseDbTours
                .whereEqualTo(CITY_TO, cityId)
                .whereGreaterThanOrEqualTo(DATE_FROM, Date(currentDay()))
                .get()
                .await()
                .forEach { document ->
                    if (document != null) {
                        resultList.add(document.convertToTourFirebaseModel())
                    }
                }
            emit(resultList)
        }

    /**
     * Search tours from city
     */
    override suspend fun searchToursFromCity(cityId: Int): Flow<List<TourFirebaseModel>> =
        flow {
            val resultList = mutableListOf<TourFirebaseModel>()
            firebaseDbTours
                .whereArrayContains(CITIES_FROM, cityId)
                .whereGreaterThanOrEqualTo(DATE_FROM, Date(currentDay()))
                .get()
                .await()
                .forEach { document ->
                    if (document != null) {
                        resultList.add(document.convertToTourFirebaseModel())
                    }
                }
            emit(resultList)
        }

    /**
     * Get upcoming tours with limit of quantity
     */
    override suspend fun soonTours(limitTours: Int): Flow<List<TourFirebaseModel>> =
        flow {
            val resultList = mutableListOf<TourFirebaseModel>()
            firebaseDbTours
                .whereGreaterThanOrEqualTo(DATE_FROM, Date(currentDay()))
                .limit(limitTours.toLong())
                .get()
                .await()
                .forEach { document ->
                    if (document != null) {
                        resultList.add(document.convertToTourFirebaseModel())
                    }
                }
            emit(resultList)
        }

    /**
     * Get all articles sorted by descending by rate field
     */
    override suspend fun getArticles(): Flow<List<ArticleFirebaseModel>> =
        flow {
            val resultList = mutableListOf<ArticleFirebaseModel>()
            firebaseInfoArticles
                .get()
                .await()
                .forEach { document ->
                    document?.let {
                        resultList.add(document.convertToArticleFirebaseModel())
                    }
                }
            resultList.sortByDescending { it.rate }
            emit(resultList)
        }

    /**
     * Get article by Id
     */
    override suspend fun getArticleById(articleId: Int): Flow<ArticleFirebaseModel> =
        flow {
            firebaseInfoArticles
                .whereEqualTo(ARTICLE_ID, articleId)
                .limit(1)
                .get()
                .await()
                .forEach { document ->
                    document?.let {
                        emit(it.convertToArticleFirebaseModel())
                    }
                }
        }

    // Fields in Firebase
    companion object {
        private const val CITIES_FROM = "citiesFrom"
        private const val CITY_TO = "cityTo"
        private const val DATE_FROM = "dateFrom"
        private const val TOUR_ID = "id"

        private const val LOG_CITIES = "cities"
        private const val LOG_DATES = "dates"
        private const val LOG_USER = "user_uid"
        private const val LOG_INFO = "info"

        private const val ARTICLE_ID = "id"

        private const val LOG_SCREEN_NAME = "screen"
        private const val LOG_SCREEN_PARAMETER = "parameter"
    }
}