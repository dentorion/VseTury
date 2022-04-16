package com.entin.presentation.screens.list

import java.io.Serializable

/**
 * List Fragment Search Action dto Main screen -> List screen
 * Used in common search to send query from Main screen to List screen and
 *
 * clicking on Main screen on slider item with one of the type SliderItemModel.action:
 * - SEARCH_TOURS_TO_CITY
 * - SEARCH_TOURS_FROM_CITY
 *
 * clicking Favourite icon on Main screen
 */

sealed class ListFragmentAction : Serializable {

    data class Search(val data: Pair<Pair<Int, Int>, Pair<Long, Long>>) : ListFragmentAction()

    data class ToursByCityTo(val data: Int) : ListFragmentAction()

    data class ToursByCityFrom(val data: Int) : ListFragmentAction()

    object Upcoming : ListFragmentAction()

    object FavouriteTours : ListFragmentAction()

    object Articles : ListFragmentAction()
}