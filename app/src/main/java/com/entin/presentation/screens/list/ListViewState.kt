package com.entin.presentation.screens.list

import com.entin.core.base.ViewState
import com.entin.presentation.model.ArticleDomainModel
import com.entin.presentation.model.TourDomainModel

data class ListViewState(
    val tours: List<TourDomainModel> = listOf(),
    val articles: List<ArticleDomainModel> = listOf(),
) : ViewState