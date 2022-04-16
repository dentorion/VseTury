package com.entin.presentation.screens.article

import com.entin.core.base.ViewState
import com.entin.presentation.model.ArticleDomainModel
import java.io.Serializable

data class ArticleViewState(
    val article: ArticleDomainModel,
) : Serializable, ViewState