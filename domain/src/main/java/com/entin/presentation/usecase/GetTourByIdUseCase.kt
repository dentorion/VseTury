package com.entin.presentation.usecase

import com.entin.presentation.model.TourDomainModel
import com.entin.presentation.repository.Repository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Get Tour from firebase by Id
 */

class GetTourByIdUseCase @Inject constructor(
    private val repository: Repository
) {
    operator fun invoke(tourId: Int): Flow<Result<TourDomainModel>> =
        repository.getTourById(tourId)
}