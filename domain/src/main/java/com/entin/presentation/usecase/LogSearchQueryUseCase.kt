package com.entin.presentation.usecase

import com.entin.presentation.repository.Repository
import javax.inject.Inject

/**
 * UseCase for logging every search query
 */

class LogSearchQueryUseCase @Inject constructor(
    private val repository: Repository
) {
    suspend operator fun invoke(
        cities: Pair<Int, Int>,
        dates: Pair<Long, Long>
    ) {
        repository.logSearch(cities, dates)
    }
}