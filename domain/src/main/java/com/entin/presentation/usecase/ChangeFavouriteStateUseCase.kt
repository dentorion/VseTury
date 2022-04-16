package com.entin.presentation.usecase

import com.entin.presentation.model.TourDomainModel
import com.entin.presentation.repository.Repository
import javax.inject.Inject

/**
 * Change favourite state of selected tour
 * by user by clicking on favourite icon
 */

class ChangeFavouriteStateUseCase @Inject constructor(
    private val repository: Repository
) {
    suspend operator fun invoke(tour: TourDomainModel, state: Boolean) {
        repository.changeFavouriteStateOfTour(tour, state)
    }
}