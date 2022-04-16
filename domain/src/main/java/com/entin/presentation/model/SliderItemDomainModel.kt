package com.entin.presentation.model

/**
 * Item of ViewPager2 on Main Fragment
 * [http] is http address
 * [text] is description
 * [sliderAction] is type of action on click:
 * INFO_PAGE -> open InfoScreenFragment with PARAMETER of article
 * SEARCH_TOURS_TO_CITY -> open ListScreenFragment with PARAMETER for search tours to city
 * WEB_OPEN -> Open web link
 */

class SliderItemModel(
    val http: String,
    val text: String,
    action: Map<String, String>,
) {
    val sliderAction: SliderAction = convertToSliderAction(action)

    /**
     * Convert Firebase text command into SliderAction for MainViewModel correct navigation
     */
    private fun convertToSliderAction(action: Map<String, String>): SliderAction {
        val type = action[TYPE] ?: INFO_PAGE
        val parameter = action[PARAMETER] ?: "0"
        return when (type) {
            INFO_PAGE -> SliderAction.ArticlePage(parameter.toInt())
            SEARCH_TOURS_TO_CITY -> SliderAction.SearchToursByCityTo(parameter.toInt())
            SEARCH_TOURS_FROM_CITY -> SliderAction.SearchToursByCityFrom(parameter.toInt())
            WEB_OPEN -> SliderAction.WebOpen(parameter)
            else -> SliderAction.Error
        }
    }

    companion object {
        private const val TYPE = "type"
        private const val PARAMETER = "parameter"
        private const val INFO_PAGE = "ArticlePage"
        private const val SEARCH_TOURS_TO_CITY = "SearchToursByCityTo"
        private const val SEARCH_TOURS_FROM_CITY = "SearchToursByCityFrom"
        private const val WEB_OPEN = "WebOpen"
    }
}

sealed class SliderAction {
    data class ArticlePage(val parameter: Int) : SliderAction()
    data class SearchToursByCityTo(val parameter: Int) : SliderAction()
    data class SearchToursByCityFrom(val parameter: Int) : SliderAction()
    data class WebOpen(val parameter: String) : SliderAction()
    object Error: SliderAction()
}