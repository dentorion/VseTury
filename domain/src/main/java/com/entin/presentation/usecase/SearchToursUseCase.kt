package com.entin.presentation.usecase

import com.entin.presentation.model.TourDomainModel
import com.entin.presentation.repository.Repository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * UseCase for logging every search query
 */

class SearchToursUseCase @Inject constructor(
    private val repository: Repository
) {
    suspend operator fun invoke(
        cityFromId: Int,
        cityToId: Int,
        dateFrom: Long,
        dateTo: Long,
    ): Flow<Result<List<TourDomainModel>>> =
        repository.searchTours(cityFromId, cityToId, dateFrom, dateTo)
}