package com.entin.presentation.screens.article

import com.entin.presentation.model.ArticleDomainModel
import java.io.Serializable

/**
 *
 */

sealed class ArticleFragmentAction : Serializable {

    data class Download(val articleId: Int) : Serializable, ArticleFragmentAction()

    data class Full(val article: ArticleDomainModel) : Serializable, ArticleFragmentAction()
}