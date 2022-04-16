package com.entin.data.extension

import com.entin.data.model.TourFirebaseModel
import com.entin.presentation.model.CityDomainModel
import com.entin.presentation.model.TourDomainModel
import com.entin.room.db.entity.FavouriteTourRoomModel
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

/**
 * Converter [TourFirebaseModel] to [TourDomainModel]
 */
suspend fun TourFirebaseModel.convertToTourDomainModel(getCityDomainModelById: suspend (Int) -> CityDomainModel?) =
    TourDomainModel(
        id = this.id,
        title = this.title,
        citiesFrom = this.citiesFrom,
        citiesFromName = this.citiesFrom.map { getCityDomainModelById(it)?.name ?: "" } as ArrayList<String>,
        cityToName = getCityDomainModelById(this.cityTo)?.name ?: "",
        cityToId = this.cityTo,
        dateFrom = SimpleDateFormat("d LLL", Locale.getDefault()).format(Date(this.dateFrom)),
        dateTo = SimpleDateFormat("d LLL", Locale.getDefault()).format(Date(this.dateTo)),
        description = this.description,
        price = this.price,
        company = this.company,
        contactPhone = this.contactPhone,
        contactName = this.contactName,
    )

/**
 * Converter LIST of [TourFirebaseModel] to LIST of [TourDomainModel]
 */
suspend fun convertListTourFirebaseModelToListTourDomainModel(
    getCityDomainModelById: suspend (Int) -> CityDomainModel?,
    firebaseModelList: List<TourFirebaseModel>
): List<TourDomainModel> {
    val domainModelList = mutableListOf<TourDomainModel>()
    firebaseModelList.forEach {
        domainModelList.add(it.convertToTourDomainModel(getCityDomainModelById))
    }
    return domainModelList.toList()
}

/**
 * Convert [FavouriteTourRoomModel] to [TourDomainModel]
 * Only short description is needed to show RecyclerView of favourite tours.
 * All other information will be downloaded by tourId after Detail Screen opened.
 */
fun FavouriteTourRoomModel.convertToTourDomainModel(): TourDomainModel =
    TourDomainModel(
        id = this.favouriteTourId,
        title = this.favouriteTourTitle,
        citiesFrom = this.favouriteTourCitiesFrom,
        citiesFromName = this.favouriteTourCitiesFromName,
        cityToName = this.favouriteTourCityToName,
        cityToId = this.favouriteTourCityToId,
        dateFrom = this.favouriteTourDateFrom,
        dateTo = this.favouriteTourDateTo,
        description = "",
        price = 0,
        company = 0,
        contactPhone = "",
        contactName = "",
    )

/**
 * Converter LIST of [FavouriteTourRoomModel] to LIST of [TourDomainModel]
 */
fun convertListFavouriteTourRoomModelToListDomainTourModel(
    favouriteTourRoomModelList: List<FavouriteTourRoomModel>
): List<TourDomainModel> {
    val domainModelList = mutableListOf<TourDomainModel>()
    favouriteTourRoomModelList.forEach {
        domainModelList.add(it.convertToTourDomainModel())
    }
    return domainModelList.toList()
}