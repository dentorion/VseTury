package com.entin.presentation.usecase

import com.entin.presentation.model.SliderItemModel
import com.entin.presentation.repository.Repository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * UseCase for getting slider items
 */

class GetSliderItemsUseCase @Inject constructor(
    private val repository: Repository
) {
    suspend operator fun invoke(): Flow<Result<List<SliderItemModel>>> =
        repository.getSliderItems()
}