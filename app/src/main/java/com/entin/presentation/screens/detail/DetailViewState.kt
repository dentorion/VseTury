package com.entin.presentation.screens.detail

import com.entin.core.base.ViewState
import com.entin.presentation.model.TourDomainModel

data class DetailViewState(
    val tourToShow: TourDomainModel,
    val favouriteState: Boolean = false,
) : ViewState