package com.entin.presentation.usecase

import com.entin.presentation.repository.Repository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Get favourite state of selected tour for favourite icon
 */

class GetFavouriteStateUseCase @Inject constructor(
    private val repository: Repository
) {
    operator fun invoke(tourId: Int): Flow<Boolean> =
        repository.getFavouriteStatusOfTour(tourId)
}