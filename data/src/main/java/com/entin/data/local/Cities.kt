package com.entin.data.local

import com.entin.data.R
import com.entin.presentation.model.CityDomainModel
import javax.inject.Inject
import javax.inject.Singleton

/**
 *  Cities for Spinner field from and to
 */

@Singleton
open class Cities @Inject constructor() {
    /**
     * All cities store
     */
    private var citiesAll: MutableList<CityDomainModel> = mutableListOf()

    init {
        citiesAll.clear()
        fillCitiesList()
    }

    /**
     * Poland cities
     */
    fun getPolandCities(): List<CityDomainModel> =
        getAllCities().filter { it.flag == Poland().flag }

    /**
     * Foreign cities
     */
    fun getForeignCities(): List<CityDomainModel> =
        getAllCities().filterNot { it.flag == Poland().flag }

    /**
     * Get City by id
     */
    fun getCityById(cityId: Int): CityDomainModel? =
        getAllCities().firstOrNull { it.id == cityId }

    /**
     * Cities All Flow
     */
    private fun getAllCities(): List<CityDomainModel> {
        return citiesAll.ifEmpty {
            fillCitiesList()
            citiesAll
        }
    }

    private fun fillCitiesList() {
        citiesAll = mutableListOf(
            // POLAND
            CityDomainModel(1, "Вроцлав", Poland().flag),
            CityDomainModel(2, "Варшава", Poland().flag),
            CityDomainModel(3, "Гданьск", Poland().flag),
            CityDomainModel(4, "Краков", Poland().flag),
            CityDomainModel(5, "Познань", Poland().flag),
            CityDomainModel(6, "Лодзь", Poland().flag),
            CityDomainModel(7, "Катовице", Poland().flag),
            CityDomainModel(8, "Легница", Poland().flag),
            CityDomainModel(9, "Гливице", Poland().flag),
            CityDomainModel(10, "Зелена Гура", Poland().flag),
            CityDomainModel(11, "Конин", Poland().flag),
            CityDomainModel(12, "Морское око", Poland().flag),
            CityDomainModel(13, "Закопане", Poland().flag),
            CityDomainModel(14, "Шклярска-Поремба", Poland().flag),
            CityDomainModel(15, "Швебодзин", Poland().flag),
            CityDomainModel(16, "Ченстохова", Poland().flag),
            CityDomainModel(17, "Быдгощ", Poland().flag),

            // GERMANY
            CityDomainModel(18, "Берлин", Germany().flag),
            CityDomainModel(19, "Дрезден", Germany().flag),
            CityDomainModel(20, "Мюнхен", Germany().flag),
            CityDomainModel(21, "Кельн", Germany().flag),

            // FRANCE
            CityDomainModel(22, "Париж", France().flag),
            CityDomainModel(23, "Марсель", France().flag),

            // CZECH
            CityDomainModel(24, "Прага", Czech().flag),
            CityDomainModel(25, "Брно", Czech().flag),

            // HOLLAND
            CityDomainModel(26, "Амстердам", Holland().flag),

            // HUNGARY
            CityDomainModel(27, "Будапешт", Hungary().flag),

            // ITALY
            CityDomainModel(28, "Венеция", Italy().flag),
            CityDomainModel(29, "Милан", Italy().flag),
            CityDomainModel(30, "Рим", Italy().flag),
            CityDomainModel(31, "Флоренция", Italy().flag),

            // AUSTRIA
            CityDomainModel(32, "Вена", Austria().flag),
            CityDomainModel(33, "Зальцбург", Austria().flag),

            // BELGIUM
            CityDomainModel(34, "Брюгге", Belgium().flag),
            CityDomainModel(35, "Брюссель", Belgium().flag),
            CityDomainModel(36, "Антверпен", Belgium().flag),
        )
    }

    /**
     * Country interface
     */
    private interface Country

    /**
     *  Countries
     */
    private data class Poland(
        val name: String = POLAND,
        val flag: Int = R.drawable.ic_country_polska
    ) : Country

    private data class Austria(
        val name: String = AUSTRIA,
        val flag: Int = R.drawable.ic_country_austria
    ) : Country

    private data class Belgium(
        val name: String = BELGIUM,
        val flag: Int = R.drawable.ic_country_belgium
    ) : Country

    private data class Czech(
        val name: String = CZECH,
        val flag: Int = R.drawable.ic_country_chech
    ) : Country

    private data class France(
        val name: String = FRANCE,
        val flag: Int = R.drawable.ic_country_france
    ) : Country

    private data class Germany(
        val name: String = GERMANY,
        val flag: Int = R.drawable.ic_country_germany
    ) : Country

    private data class Holland(
        val name: String = HOLLAND,
        val flag: Int = R.drawable.ic_country_holland
    ) : Country

    private data class Italy(
        val name: String = ITALY,
        val flag: Int = R.drawable.ic_country_italy
    ) : Country

    private data class Hungary(
        val name: String = HUNGARY,
        val flag: Int = R.drawable.ic_country_wengry
    ) : Country

    companion object {
        const val POLAND = "Poland"
        const val AUSTRIA = "Austria"
        const val BELGIUM = "Belgium"
        const val CZECH = "Czech"
        const val FRANCE = "France"
        const val GERMANY = "Germany"
        const val HOLLAND = "Holland"
        const val ITALY = "Italy"
        const val HUNGARY = "Hungary"
    }
}