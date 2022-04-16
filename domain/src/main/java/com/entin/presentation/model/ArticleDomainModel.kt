package com.entin.presentation.model

import java.io.Serializable

/**
 * InfoArticleModel - model for domain level of Article for Info screen
 */

data class ArticleDomainModel(
    val title: String,
    val text: String,
    val rate: Int,
    val images: ArrayList<String>,
    val id: Int,
    val date: Long,
    val comment: String = "",
    val actionButton: Map<String, String>,
) : Serializable {
    val articleButtonAction: ArticleButtonAction = convertToArticleButtonAction(actionButton)

    /**
     * Convert Firebase text command into ActionButtonAction for InfoDetailScreenViewModel correct navigation
     */
    private fun convertToArticleButtonAction(action: Map<String, String>): ArticleButtonAction {
        val type = action[TYPE]
        val parameter = action[PARAMETER] ?: "0"
        return when (type) {
            SEARCH_TOUR -> ArticleButtonAction.SearchTourById(parameter.toInt())
            SEARCH_TOURS_TO_CITY -> ArticleButtonAction.SearchToursByCityTo(parameter.toInt())
            SEARCH_TOURS_FROM_CITY -> ArticleButtonAction.SearchToursByCityFrom(parameter.toInt())
            WEB_OPEN -> ArticleButtonAction.WebOpen(parameter)
            else -> ArticleButtonAction.Error
        }
    }

    companion object {
        private const val TYPE = "type"
        private const val PARAMETER = "parameter"
        private const val SEARCH_TOUR = "SearchTourById"
        private const val SEARCH_TOURS_TO_CITY = "SearchToursByCityTo"
        private const val SEARCH_TOURS_FROM_CITY = "SearchToursByCityFrom"
        private const val WEB_OPEN = "WebOpen"
    }
}

sealed class ArticleButtonAction: Serializable {
    data class SearchTourById(val parameter: Int) : Serializable, ArticleButtonAction()
    data class SearchToursByCityTo(val parameter: Int) : Serializable, ArticleButtonAction()
    data class SearchToursByCityFrom(val parameter: Int) : Serializable, ArticleButtonAction()
    data class WebOpen(val parameter: String) : Serializable, ArticleButtonAction()
    object Error : Serializable, ArticleButtonAction()
}