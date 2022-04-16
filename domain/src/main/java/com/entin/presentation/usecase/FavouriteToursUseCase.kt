package com.entin.presentation.usecase

import com.entin.presentation.model.TourDomainModel
import com.entin.presentation.repository.Repository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * List of favourite tours saved by Room
 */

class FavouriteToursUseCase @Inject constructor(
    private val repository: Repository
) {
    operator fun invoke(): Flow<Result<List<TourDomainModel>>> =
        repository.favouriteTours()
}