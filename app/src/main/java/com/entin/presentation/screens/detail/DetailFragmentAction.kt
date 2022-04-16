package com.entin.presentation.screens.detail

import com.entin.presentation.model.TourDomainModel
import java.io.Serializable

/**
 * Detail Fragment Action dto List screen -> Detail screen
 * if Favourite List -> Need to be downloaded full data
 *    All others Lists -> Only open parameter TourDomainModel
 */

sealed class DetailFragmentAction : Serializable {

    data class Download(val tourId: Int) : DetailFragmentAction()

    data class Full(val tour: TourDomainModel) : DetailFragmentAction()
}