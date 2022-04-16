package com.entin.presentation.usecase

import com.entin.presentation.model.TourDomainModel
import com.entin.presentation.repository.Repository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Get soon tours
 */

class GetToursCityFromUseCase @Inject constructor(
    private val repository: Repository
) {
    suspend operator fun invoke(cityId: Int): Flow<Result<List<TourDomainModel>>> =
        repository.searchToursFromCity(cityId)
}