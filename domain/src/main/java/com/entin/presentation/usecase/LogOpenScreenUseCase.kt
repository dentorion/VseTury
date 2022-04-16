package com.entin.presentation.usecase

import com.entin.presentation.model.LogOpenScreen
import com.entin.presentation.repository.Repository
import javax.inject.Inject

/**
 * UseCase for logging every search opened screen
 */

class LogOpenScreenUseCase @Inject constructor(
    private val repository: Repository
) {
    suspend operator fun invoke(screenName: String, parameter: String) =
        repository.logOpenTour(
            LogOpenScreen(screenName, parameter)
        )
}