package com.entin.presentation.usecase

import com.entin.presentation.model.TourDomainModel
import com.entin.presentation.repository.Repository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Get soon tours
 */

class GetUpcomingToursUseCase @Inject constructor(
    private val repository: Repository
) {
    suspend operator fun invoke(): Flow<Result<List<TourDomainModel>>> =
        repository.getUpcomingTours(QUANTITY_LIMIT)

    companion object {
        private const val QUANTITY_LIMIT = 10
    }
}